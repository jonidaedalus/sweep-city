package kz.alash.naklei.service.esb.dto.route;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RouteCreateRequest implements Serializable {

    //delete
    private String advertisementID;
    private List<Coordinate> coordinates;
    //earned points by driver during this route
    private Double points; //points -> earnedMoney
    private Double distance;
    private Double ots;
    private Date startDate;
    private Date endDate;

    //маржа
    private Double income; //earnedPoints -> incomeSweep

    public Double getIncome() {
        return income;
    }

    public class Coordinate implements Serializable{
        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

    }

    public Double getDistance() {
        return distance;
    }

    public Double getOts() {
        return ots;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getAdvertisementID() {
        return advertisementID;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Double getPoints() {
        return points;
    }
}
