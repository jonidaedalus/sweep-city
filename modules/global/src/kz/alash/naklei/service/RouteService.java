package kz.alash.naklei.service;

import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.service.esb.dto.route.RouteCreateRequest;
import kz.alash.naklei.service.esb.dto.route.RouteCreateResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.util.List;

public interface RouteService {
    String NAME = "naklei_RouteService";

    RouteCreateResponse createRoute(RouteCreateRequest routeRequest);

    LineString getLineFromCoordinates(List<Coordinate> points);

    Double calculateDistance(Geometry lineString);

    Double calculatePoints(DZone zone, LineString line);
}