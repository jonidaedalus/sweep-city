package kz.alash.naklei.web.screens.dzone;

import com.haulmont.addon.maps.web.gui.components.CanvasLayer;
import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.layer.style.PolygonStyle;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@UiController("naklei_DZone.browse")
@UiDescriptor("d-zone-browse.xml")
@LookupComponent("zonesTable")
public class DZoneBrowse extends StandardLookup<DZone> {
    @Inject
    private GeoMap map;
    @Inject
    private GroupTable<DZone> zonesTable;
    @Inject
    private CollectionLoader<DCity> dCitiesDl;
    @Inject
    private LookupField<DCity> cityFilter;
    @Inject
    private CollectionContainer<DCity> dCitiesDc;
    @Inject
    private CollectionLoader<DZone> dZonesDl;
    @Inject
    private CollectionContainer<DZone> dZonesDc;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        dCitiesDl.load();
        dCitiesDc.getItems().stream()
                .filter(dCity -> dCity.getCode().equals(DCity.ALMATY_CITY_CODE))
                .findAny()
                .ifPresent(city -> cityFilter.setValue(city));
    }

    @Subscribe("cityFilter")
    public void onCityFilterValueChange(HasValue.ValueChangeEvent<DCity> event) {
        DCity city = event.getValue();
        if(city != null){
            changeMapCenter(city);
            refreshZones(city);
        }
    }

    private void refreshZones(DCity city) {
        dZonesDl.setParameter("city", city);
        dZonesDl.load();

        refreshCanvasLayer(null);
    }

    private void refreshCanvasLayer(DZone selectedZone) {
        List<DZone> zones = dZonesDc.getItems();
        CanvasLayer canvasLayer = map.getCanvas();
        canvasLayer.clear();
        if(zones.size() > 0){
            zones.forEach(zone -> {
                CanvasLayer.Polygon polygon = canvasLayer.addPolygon(zone.getPolygon());
                if(selectedZone != null && selectedZone.equals(zone)){
                    polygon.setStyle(new PolygonStyle().setFillColor("green").setStrokeColor("darkgreen"));
                }else{
                    polygon.setStyle(new PolygonStyle().setFillColor("grey").setStrokeColor("black"));
                    polygon.addClickListener(clickEvent -> zonesTable.setSelected(zone));
                }
            });
        }
    }

    private void changeMapCenter(DCity city) {
        map.setCenter(city.getXpoint(), city.getYpoint());
        map.setZoomLevel(city.getZoomLevel());
    }

    @Subscribe("zonesTable")
    public void onZonesTableSelection(Table.SelectionEvent<DZone> event) {
        Set<DZone> selectedZones = event.getSelected();
        //т.к таблица выбирает только одну - multiselect="false"
        if(selectedZones.size() == 1){
            DZone selected = selectedZones.iterator().next();
            refreshZones(selected.getCity());
            refreshCanvasLayer(selected);
        }
    }
}