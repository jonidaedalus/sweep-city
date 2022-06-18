package kz.alash.naklei.web.screens.advertisementdriver;

import com.haulmont.addon.maps.web.gui.components.CanvasLayer;
import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.HeatMapOptions;
import com.haulmont.addon.maps.web.gui.components.layer.style.PolygonStyle;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Route;
import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.service.RouteService;
import org.locationtech.jts.geom.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UiController("naklei_AdvDriverShowFragment")
@UiDescriptor("adv-driver-show-fragment.xml")
public class AdvDriverShowFragment extends ScreenFragment {
    private AdvertisementDriver advDriver;
    @Inject
    private InstanceContainer<AdvertisementDriver> advDriverDc;
    @Inject
    private CollectionLoader<DZone> zonesDl;
    @Inject
    private GeoMap map;
    @Inject
    private InstanceContainer<Route> selectedRouteDc;
    @Inject
    private KeyValueCollectionLoader zonePointsDl;
    @Inject
    private RouteService routeService;
    @Inject
    private GroupTable<Route> routesGroupTable;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionContainer<DZone> zonesDc;
    @Inject
    private DataManager dataManager;
//    @Named("map.zonesLayerVector")
//    private VectorLayer<DZone> zonesLayerVector;

    public void setAdvDriver(AdvertisementDriver advDriver){
        this.advDriver = advDriver;
    }


    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onBeforeShowHost(Screen.BeforeShowEvent event) {
        if(advDriver == null)
            return;

        advDriver = dataManager.reload(advDriver, "show-adv-driver-view");

        advDriverDc.setItem(advDriver);
        zonesDl.setParameter("city", advDriver.getPurpose().getAdvertisement().getCity());
        zonesDl.load();

        //Цвета для разных типов зон
//        Layer zonesLayerVector = map.getLayerOrNull("zonesLayerVector");
//        if(zonesLayerVector != null){
//            ((VectorLayer<DZone>)zonesLayerVector).setStyleProvider(dZone -> {
//                if(dZone.getZoneType()!=null && dZone.getZoneType().getCode() != null && dZone.getZoneType().getCode().contains("1")){
//                    return new PolygonStyle().setFillColor("green").setStrokeColor("darkgreen").setStrokeWeight(2).setFillOpacity(0.2);
//                }
//                else {
//                    return new PolygonStyle().setFillColor("red").setStrokeColor("darkred").setStrokeWeight(2).setFillOpacity(0.2);
//                }
//            });
//        }
        Map<Point, Double> newMap = advDriver.getRoutes().stream()
                .flatMap(route -> {
                    Coordinate[] coordinates = route.getLine().getCoordinates();
                    GeometryFactory factory = new GeometryFactory();
                    MultiPoint multiPoint = factory.createMultiPointFromCoords(coordinates);
                    List<Point> points = new ArrayList<>();
                    for (int i = 0; i < multiPoint.getNumGeometries(); i++){
                        Point point = factory.createPoint(multiPoint.getGeometryN(i).getCoordinate());
                        points.add(point);
                    }
                    return points.stream();
                })
                .collect(Collectors.toMap(point -> point, multiPoint -> 1D, Double::sum));

        HeatMapOptions options = new HeatMapOptions();
        options.setMaximumIntensity(5D);
        options.setMinOpacity(0.4D);

        map.addHeatMap(newMap, options);
    }

    @Install(to = "zonePointsDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> zonePointsDlLoadDelegate(ValueLoadContext valueLoadContext) {
        Route selectedRoute = null;

        if(routesGroupTable.getSelected().size() > 0)
            selectedRoute = routesGroupTable.getSelected().iterator().next();

        if(selectedRoute == null)
            return null;

        List<KeyValueEntity> list = new ArrayList<>();

        for (DZone zone :  zonesDc.getItems()) {
            Double points = routeService.calculatePoints(zone, selectedRoute.getLine());
            if(points > 0.0){
                Geometry geo = selectedRoute.getLine().intersection(zone.getPolygon());
                List<LineString> lines = new ArrayList<>();
                if(geo instanceof MultiLineString){
                    MultiLineString multiLineString = (MultiLineString) geo;
                    if(multiLineString.getNumGeometries() > 0){
                        for (int i = 0; i < multiLineString.getNumGeometries(); i++){
                            LineString line = (LineString) multiLineString.getGeometryN(i);
                           lines.add(line);
                        }
                    }
                }else {
                    LineString lineInZone = (LineString) selectedRoute.getLine().intersection(zone.getPolygon());
                    lines.add(lineInZone);
                }

                lines.forEach(lineString -> {
                    KeyValueEntity element = new KeyValueEntity();
                    element.setIdName("zoneId");
                    element.setId(zone.getId());
                    element.setValue("zone", zone);
                    element.setValue("points", points);
                    element.setValue("distance", routeService.calculateDistance(lineString));

                    list.add(element);
                });
            }
        }

        return list;
    }

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

    @Subscribe("routesGroupTable")
    public void onRoutesGroupTableSelection(Table.SelectionEvent<Route> event) {
        Route selectedRoute = event.getSelected().stream().findFirst().orElse(null);
        if(selectedRoute != null){
            selectedRouteDc.setItem(selectedRoute);
            setSelectedRouteToCanvas(selectedRoute.getLine());

            zonePointsDl.load();
        }

//        selectedRoute
//                .getAdvertisementDriver()
//                .getPurpose()
//                .getAdvertisement()
//                .getCity()
//                .getZones().forEach(
//                        zone -> {
//                            Double points = routeService.calculatePoints(zone, selectedRoute.getLine());
//                            if(points > 0){
//                                notifications
//                                        .create()
//                                        .withDescription(
//                                                "В зоне " +
//                                                        zone.getName() +
//                                                        " заработано поинтов " +
//                                                        points)
//                                        .show();
//                                LineString lineInZone = (LineString) selectedRoute.getLine().intersection(zone.getPolygon());
//                                setSelectedRouteToCanvas(lineInZone);
//                            }
//                        }
//                );
    }
    
    
}