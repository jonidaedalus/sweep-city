package kz.alash.naklei.web.screens.advertisementdriver;

import com.haulmont.addon.maps.web.gui.components.CanvasLayer;
import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.layer.style.PolygonStyle;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Advertisement;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Route;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.service.CityService;
import kz.alash.naklei.service.RouteService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("naklei_AdvertisementDriver.show")
@UiDescriptor("advertisement-driver-show.xml")
@EditedEntityContainer("advertisementDriverDc")
@LoadDataBeforeShow
public class AdvertisementDriverShow extends StandardEditor<AdvertisementDriver> {

    @Inject
    private RouteService routeService;

    private Advertisement advertisement;
    @Inject
    private CityService cityService;
    @Inject
    private GeoMap map;
    @Inject
    private DataManager dataManager;

    public void setAdvertisement(Advertisement advertisement){
        this.advertisement = advertisement;
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        AdvertisementDriver advDriver = dataManager.reload(getEditedEntity(), "show-adv-driver-view");
        setEntityToEdit(advDriver);
    }



    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        List<Route> routes = getEditedEntity().getRoutes();

        if(routes.size() > 0){
            CanvasLayer canvas = map.getCanvas();
            canvas.clear();
            routes.forEach(route -> {
                CanvasLayer.Polyline canvasLine = canvas.addPolyline(route.getLine());
                canvasLine.setStyle(
                        new PolygonStyle()
                                .setFillColor("black")
                                .setStrokeColor("black")
                                .setStrokeWeight(7)
                                .setStrokeOpacity(0.5)
                );
            });
        }
    }
    
    

    @Install(to = "zonePointsDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> zonePointsDlLoadDelegate(ValueLoadContext valueLoadContext) {

        List<KeyValueEntity> list = new ArrayList<>();

        DCity city = advertisement.getCity();

        List<DZone> cityZones = cityService.getCityZones(city);

        for(DZone zone : cityZones){
            for(Route route : getEditedEntity().getRoutes()){
                Double points = routeService.calculatePoints(zone, route.getLine());
                if(points > 0.0){
                    Geometry lineInZone = route.getLine().intersection(zone.getPolygon());

                    KeyValueEntity element = new KeyValueEntity();
                    element.setIdName("zoneId");
                    element.setId(zone.getId());
                    element.setValue("zone", zone.getZoneType().getName());
                    element.setValue("points", points);
                    element.setValue("distance", routeService.calculateDistance(lineInZone));

                    list.add(element);
                }
            }
        }

        return list;
    }
}