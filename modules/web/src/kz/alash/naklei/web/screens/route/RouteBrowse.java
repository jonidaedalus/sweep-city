package kz.alash.naklei.web.screens.route;

import com.haulmont.addon.maps.web.gui.components.CanvasLayer;
import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.layer.style.PolygonStyle;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Route;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.service.RouteService;
import org.locationtech.jts.geom.LineString;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UiController("naklei_Route.browse")
@UiDescriptor("route-browse.xml")
@LookupComponent("routesTable")
@LoadDataBeforeShow
public class RouteBrowse extends StandardLookup<Route> {
    @Inject
    private CollectionContainer<Route> routesDc;
    @Inject
    private GeoMap map;
    @Inject
    private VBoxLayout pointsInfo;
    @Inject
    private InstanceContainer<Route> selectedRouteDc;
    @Inject
    private TextField<Double> resultsKilometer;
    @Inject
    private RouteService routeService;
    @Inject
    private Notifications notifications;
    @Inject
    private GroupTable<Route> routesTable;
    @Inject
    private KeyValueCollectionLoader zonePointsDl;

//    @Subscribe
//    public void onAfterShow(AfterShowEvent event) {
//        List<Route> routes = routesDc.getItems();
//
//        if(routes.size() > 0){
//            setRoutesToCanvas(routes);
//        }
//    }

    private void setSelectedRouteToCanvas(LineString line) {
        CanvasLayer canvasLayer = map.getCanvas();
        canvasLayer.clear();

        CanvasLayer.Polyline canvasLine = canvasLayer.addPolyline(line);
        canvasLine.setStyle(
                new PolygonStyle()
                        .setFillColor("green")
                        .setStrokeColor("darkgreen")
                        .setStrokeWeight(2)
                        .setFillOpacity(0.2)
        );

    }

    @Subscribe("routesTable")
    public void onRoutesTableSelection(Table.SelectionEvent<Route> event) {
        Route selectedRoute = event.getSelected().iterator().next();
        selectedRouteDc.setItem(selectedRoute);
        setSelectedRouteToCanvas(selectedRoute.getLine());

        pointsInfo.setVisible(true);
        zonePointsDl.load();
        selectedRoute
                .getAdvertisementDriver()
                .getPurpose()
                .getAdvertisement()
                .getCity()
                .getZones().forEach(
                        zone -> {
                            Double points = routeService.calculatePoints(zone, selectedRoute.getLine());
                            if(points > 0){
                                notifications
                                        .create()
                                        .withDescription(
                                                "В зоне " +
                                                zone.getName() +
                                                " заработано поинтов " +
                                                points)
                                        .show();
                                LineString lineInZone = (LineString) selectedRoute.getLine().intersection(zone.getPolygon());
                                setSelectedRouteToCanvas(lineInZone);
                            }
                        }
        );
        //resultsKilometer.setValue(selectedRoute.getLine().d());
    }

    @Install(to = "zonePointsDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> zonePointsDlLoadDelegate(ValueLoadContext valueLoadContext) {
        Route selectedRoute = null;

        if(routesTable.getSelected().size() > 0)
            selectedRoute = routesTable.getSelected().iterator().next();

        if(selectedRoute == null)
            return null;

        List<KeyValueEntity> list = new ArrayList<>();

        DCity city = selectedRoute.getAdvertisementDriver().getPurpose().getAdvertisement().getCity();

        for(DZone zone : city.getZones()){
            Double points = routeService.calculatePoints(zone, selectedRoute.getLine());
            if(points > 0.0){
                LineString lineInZone = (LineString) selectedRoute.getLine().intersection(zone.getPolygon());

                KeyValueEntity element = new KeyValueEntity();
                element.setIdName("zoneId");
                element.setId(zone.getId());
                element.setValue("zone", zone);
                element.setValue("points", points);
                element.setValue("distance", routeService.calculateDistance(lineInZone));

                list.add(element);
            }
        }

        return list;
    }

}