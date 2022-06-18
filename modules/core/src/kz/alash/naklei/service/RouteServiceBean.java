package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.Route;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;
import kz.alash.naklei.service.esb.dto.route.RouteCreateRequest;
import kz.alash.naklei.service.esb.dto.route.RouteCreateResponse;
import org.locationtech.jts.geom.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service(RouteService.NAME)
public class RouteServiceBean implements RouteService {
    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Inject
    private DataManager dataManager;
    @Inject
    private Authentication authentication;
    @Inject
    private Logger log;
    @Inject
    private DriverService driverService;

    @Override
    public RouteCreateResponse createRoute(RouteCreateRequest routeRequest) {
        //todo Проверить работоспосбность метода
        long start = System.currentTimeMillis();

        RouteCreateResponse response = new RouteCreateResponse();
        RouteCreateResponse.Result result = new RouteCreateResponse.Result();
        response.setResult(result);

        User user = authentication.begin().getUser();

        try {
            Route route = dataManager.create(Route.class);

            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties(
                            "advertisementDrivers",
                            "advertisementDrivers.driver",
                            "advertisementDrivers.endDate",
                            "advertisementDrivers.routes",
                            "advertisementDrivers.totalRun",
                            "advertisementDrivers.earnedMoney",
                            "advertisementDrivers.currentMoney",
                            "earnedMoney",
                            "currentMoney",
                            "totalRun"
                    )
                    .one();

            AdvertisementDriver advertisementDriver =
                    driver.getAdvertisementDrivers().stream()
                            .filter(advDriver -> advDriver.getEndDate() == null)
                            .findFirst()
                            .orElse(null);

            List<Coordinate> coordinates = routeRequest.getCoordinates().stream()
                    .map(coordinate -> new Coordinate(coordinate.getLatitude(), coordinate.getLongitude())).collect(Collectors.toList());

            LineString line = getLineFromCoordinates(coordinates);

            route.setLine(line);


            if(advertisementDriver == null)
                throw new Exception();

            route.setAdvertisementDriver(advertisementDriver);

            //Высчитываем пройденную дистанцию
            //Double distance = calculateDistance(line);

            route.setDistance(routeRequest.getDistance());
            route.setDate(dateFormat.parse(dateFormat.format(routeRequest.getStartDate())));
            route.setOts(routeRequest.getOts());
            int spentMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(routeRequest.getEndDate().getTime() - routeRequest.getStartDate().getTime());
            route.setTime(spentMinutes / 60.0);

            if (advertisementDriver.getTotalRun() == null)
                advertisementDriver.setTotalRun(0);

            advertisementDriver.setTotalRun(advertisementDriver.getTotalRun() + routeRequest.getDistance().intValue());

            //Высчитываем колличество поинтов // уже не нужно считать, берем с мобилки
//            Double points = calculatePoints(advertisement.getCity(), line);
            //обновляем поинты для каждой из сущностей
            route.setPoints(routeRequest.getPoints());
//            if (driver.getEarnedMoney() == null)
//                driver.setEarnedMoney(BigDecimal.ZERO);
//            if (advertisementDriver.getEarnedMoney() == null)
//                advertisementDriver.setEarnedMoney(BigDecimal.ZERO);
//            if (driver.getCurrentMoney() == null)
//                driver.setCurrentMoney(BigDecimal.ZERO);

            driver.setCurrentMoney(driver.getCurrentMoney().add(BigDecimal.valueOf(routeRequest.getPoints())));
            driver.setEarnedMoney(driver.getEarnedMoney().add(BigDecimal.valueOf(routeRequest.getPoints())));
            driver.setTotalRun(driver.getTotalRun() + routeRequest.getDistance());

            advertisementDriver.setEarnedMoney(advertisementDriver.getEarnedMoney().add(BigDecimal.valueOf(route.getPoints())));
            advertisementDriver.setCurrentMoney(advertisementDriver.getCurrentMoney().add(BigDecimal.valueOf(route.getPoints())));
            //транзакция
            CommitContext context = new CommitContext();
            context.addInstanceToCommit(route);
            context.addInstanceToCommit(driver);
            context.addInstanceToCommit(advertisementDriver);

            dataManager.commit(context);

            result.setDriverCurrentPoints(driver.getCurrentMoney().doubleValue());
            result.setDistance(routeRequest.getDistance());
            result.setPoints(routeRequest.getPoints());
            response.setCode("0");
        } catch (Exception e) {
            response.setCode("1");
            response.setMessage("Ошибка при сохранении маршрута: " + e.getMessage());
            log.error("Error: routeServiceBean/createRoute. ErrorMessage: " + e);
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    /**
     * @param points - list of points in [longitude, lattitude] format
     * @return A route that driver made through the points
     */
    @Override
    public LineString getLineFromCoordinates(List<Coordinate> points) {
//        ArrayList<Coordinate> points = new ArrayList<>();
//        points.add(new Coordinate(43, 81));
//        points.add(new Coordinate(44, 83));
        GeometryFactory factory = new GeometryFactory();
        return factory.createLineString(points.toArray(new Coordinate[] {}));
    }


    // метод который умеет считать деньги/поинты в для определенной зоны
    @Override
    public Double calculatePoints(DZone zone, LineString line) {
        Geometry lineInZone = line.intersection(zone.getPolygon());

        if(lineInZone == null || lineInZone.isEmpty())
            return 0.0;

        Double distanceInZone = calculateDistance(lineInZone);

        return distanceInZone * zone.getZoneType().getPointCoef();
//        List<DZone> driver = dataManager.load(DZone.class)
//                .query("select c from naklei_Driver c where c.user.id = :userId")
//                .parameter("userId", user.getId())
//                .viewProperties(
//                        "advertisementDrivers",
//                        "advertisementDrivers.driver",
//                        "advertisementDrivers.advertisement",
//                        "advertisementDrivers.routes"
//                )
//                .one();

        //LineString intersection = (LineString) zone.getPolygon().intersection(route.getLine());

        //нужно будет глянуть какую дистанцию он высчитывает (вдруг просто по ширине/долготе выдает, а нам нужны километры)
        //мб нужно будет перевести в километры
        //BigDecimal distance = BigDecimal.valueOf(intersection.getLength());
        //return (double)0;
//        return distance
//                .multiply(BigDecimal.valueOf(zone.getCoverageCoef().getCoefficientValue()))
//                .multiply(BigDecimal.valueOf(zone.getPointCoef().getCoefficientValue()));
    }

    private Double calculatePoints(DCity city, LineString line){
        city = dataManager.reload(city, "only-zones");

        Double result = (double) 0;

        for(DZone zone : city.getZones()){
            result += calculatePoints(zone,line);
        }

        return result;
    }


    /**
     * @param lat1 - широта первой точки
     * @param lon1 - долгота первой точки
     * @param lat2 - широта второй точки
     * @param lon2 - долгота второй точки
     * @return - дистанция в километрах между двумя точками
     */
    private Double calculateHaversine(Double lat1, Double lon1, Double lat2, Double lon2) {
        //дистанция между точками
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        //переводим в радианы
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        //формула Haversine
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);

        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));

        return rad * c;
    }


    /**
     * @param lineString - геометрическая линия состоящая из Coordinate[]
     * @return - длина данной линии в километрах
     */
    @Override
    public Double calculateDistance(Geometry lineString) {
        double distance = 0.0;
        if (lineString instanceof  LineString){
            distance = getDistance(lineString);
        }else if(lineString instanceof MultiLineString){
            MultiLineString multiLineString = (MultiLineString) lineString;
                if(multiLineString.getNumGeometries() > 0){
                    for (int i = 0; i < multiLineString.getNumGeometries(); i++){
                        LineString line = (LineString) multiLineString.getGeometryN(i);
                        distance += getDistance(line);
                    }
                }
        }

        return distance;
    }

    private double getDistance(Geometry lineString) {
        Coordinate[] points = lineString.getCoordinates();

        double distance = 0;
        //todo нужно уточнить очередность длины и широты, я не уверен что x это latitude
        for (int i = 1; i < points.length; i++) {
            distance += calculateHaversine(points[i].x, points[i].y, points[i-1].x,  points[i-1].y);
        }
        return distance;
    }

}