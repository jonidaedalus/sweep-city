package kz.alash.naklei.web.screens.dzone;

import com.haulmont.addon.maps.web.gui.components.CanvasLayer;
import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.layer.style.PolygonStyle;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Form;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.entity.dict.DZoneType;
import org.locationtech.jts.geom.Polygon;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("naklei_DZone.edit")
@UiDescriptor("d-zone-edit.xml")
@EditedEntityContainer("dZoneDc")
@LoadDataBeforeShow
public class DZoneEdit extends StandardEditor<DZone> {
    //Форма заполнения - ФЗ
    @Inject
    private Form form;
    @Inject
    private CollectionLoader<DZone> cityZonesDl;
    @Inject
    private CollectionContainer<DZone> cityZonesDc;
    @Inject
    private GeoMap map;
    @Inject
    private CollectionLoader<DCity> citiesDl;
    @Inject
    private Notifications notifications;

    private DZone cityZone;
    @Inject
    private CollectionContainer<DCity> citiesDc;
    @Inject
    private LookupPickerField<DCity> cityField;
    @Inject
    private DataManager dataManager;

    private boolean otherZonesBroken;
    @Inject
    private CheckBox mainField;
    @Inject
    private CollectionLoader<DZoneType> zoneTypesDl;
    @Inject
    private CollectionContainer<DZoneType> zoneTypesDc;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        citiesDl.load();
        DCity city = getEditedEntity().getCity();
        if(city == null){
            citiesDc.getItems().stream()
                    .filter(dCity -> dCity.getCode().equals(DCity.ALMATY_CITY_CODE))
                    .findAny()
                    .ifPresent(almatyCity -> cityField.setValue(almatyCity));
            city = cityField.getValue();
        }

        zoneTypesDl.setParameter("city", city);
        zoneTypesDl.load();

    }

    @Subscribe(id = "dZoneDc", target = Target.DATA_CONTAINER)
    public void onDZoneDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<DZone> event) {
        if(event.getProperty().equals("polygon") && cityZonesDc.getItems().size() > 0){
            List<DZone> changedZones = new ArrayList<>();
            otherZonesBroken = false;
            //из других зон вырежим нашу текущую зону
            cityZonesDc.getItems().forEach(zone -> {
                if(!zone.equals(getEditedEntity())){
                    Polygon zonePolygon = zone.getPolygon();
                    Polygon currentZonePolygon = getEditedEntity().getPolygon();

                    Polygon newZonePolygon;
                    try{
                        newZonePolygon = (Polygon) zonePolygon.difference(currentZonePolygon);
                    }catch (Exception e){
                        otherZonesBroken = true;
                        notifications.create(Notifications.NotificationType.ERROR)
                                .withCaption("Ошибка")
                                .withDescription("Текущая зона разделила зону \"" + zone.getName() + "\" на два полигона")
                                .show();
                        return;
                    }

                    if(!zonePolygon.isEmpty() && !newZonePolygon.equals(zonePolygon)){
                        zone = dataManager.reload(zone, "dZone-edit");
                        zone.setPolygon(newZonePolygon);

                        changedZones.add(zone);
                    }
                }
            });

            if(changedZones.size() > 0){
                getScreenData().getDataContext()
                        .addPostCommitListener(postCommitEvent -> changedZones.forEach(zone -> dataManager.commit(zone)));
            }
        }
    }
    
    

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        Boolean isCityCheckBoxValue = mainField.getValue();
        if(cityZone == null && isCityCheckBoxValue != null && !isCityCheckBoxValue){
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption("Ошибка")
                    .withDescription("Создайте полигон для города " + cityField.getValue().getName())
                    .show();
            event.preventCommit();
        }

        if(otherZonesBroken){
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption("Ошибка")
                    .withDescription("Имеются зоны, которые при пересении с текущей зоной, разделились на два или более полигона")
                    .show();
            event.preventCommit();
        }
    }

    @Subscribe("cityField")
    public void onCityFieldValueChange(HasValue.ValueChangeEvent<DCity> event) {
        DCity city = event.getValue();
        if(city != null){
            changeMapCenter(city);
            //Если зона города не была создана - открываем ФЗ
            refreshCityZones(city);

            List<DZone> cityZones = cityZonesDc.getItems();

            if(cityZones.size() > 0){
                cityZone = cityZones.stream().filter(DZone::getMain).findAny().orElse(null);

                createCityZonesLayer();
            }else{
                cityZone = null;
            }

            if(cityZone == null || cityZones.size() == 0)
                notifications.create(Notifications.NotificationType.WARNING)
                        .withCaption("Внимание")
                        .withDescription("Не создана зона города " + city.getName())
                        .show();


            setFormEnable(true);
        }
    }

    private void createCityZonesLayer() {
        CanvasLayer canvasLayer = map.getCanvas();
        canvasLayer.clear();

        cityZonesDc.getItems().forEach(zone -> {
            CanvasLayer.Polygon canvasPolygon = canvasLayer.addPolygon(zone.getPolygon());
            if(zone.equals(getEditedEntity()))
                canvasPolygon.setStyle(new PolygonStyle().setFillColor("green").setStrokeColor("darkgreen").setStrokeWeight(2).setFillOpacity(0.2));
            else
                canvasPolygon.setStyle(new PolygonStyle().setFillColor("red").setStrokeColor("darkred").setStrokeWeight(2).setFillOpacity(0.2));
        });
    }

    private void refreshCityZones(DCity city) {
        cityZonesDl.setParameter("city", city);
        cityZonesDl.load();
    }

    private void changeMapCenter(DCity city) {
        map.setCenter(city.getXpoint(), city.getYpoint());
        map.setZoomLevel(city.getZoomLevel());
    }

    private void setFormEnable(boolean enable) {
        form.getComponents().forEach(component -> component.setEnabled(enable));
    }

    @Subscribe("mainField")
    public void onMainFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        Boolean isCity = event.getValue();
        setFormEnable(isCity != null);
    }

//    @Subscribe("map")
//    public void onMapClick(GeoMap.ClickEvent event) {
//        notifications.create().withDescription("x - " + event.getPoint().getX() + ",y - " + event.getPoint().getY()).show();
//    }

//    @Subscribe("test")
//    public void onTestClick(Button.ClickEvent event) {
//        Polygon zonePolygon = cityZone.getPolygon();
//        Polygon currentZonePolygon = getEditedEntity().getPolygon();
//
//        Polygon newZonePolygon = null;
//        try{
//            newZonePolygon = (Polygon) zonePolygon.difference(currentZonePolygon);
//        }catch (Exception e){
//            notifications.create(Notifications.NotificationType.ERROR)
//                    .withCaption("Ошибка")
//                    .withDescription("Текущая зона разделила зону \"" + cityZone.getName() + "\" на два полигона")
//                    .show();
//            return;
//        }
//        CanvasLayer canvas = map.getCanvas();
//        canvas.clear();
//        CanvasLayer.Polygon cPolygon = canvas.addPolygon(newZonePolygon);
//        cPolygon.setStyle(new PolygonStyle().setFillColor("green").setStrokeColor("darkgreen").setStrokeWeight(2).setFillOpacity(0.2));
//
//    }
}