package kz.alash.naklei.service.esb.dto.route;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;

public class RouteCreateResponse extends GenericResponse<RouteCreateResponse.Result> {
    public static class Result implements Serializable {
        private String advertisementID;
        private String driverID;
        private Double points;
        private Double driverCurrentPoints;
        private Double distance;

        public Double getDriverCurrentPoints() {
            return driverCurrentPoints;
        }

        public void setDriverCurrentPoints(Double driverCurrentPoints) {
            this.driverCurrentPoints = driverCurrentPoints;
        }

        public Double getPoints() { return points;}

        public void setPoints(Double points) { this.points = points; }

        public String getAdvertisementID() {
            return advertisementID;
        }

        public void setAdvertisementID(String advertisementID) {
            this.advertisementID = advertisementID;
        }

        public String getDriverID() {
            return driverID;
        }

        public void setDriverID(String driverID) {
            this.driverID = driverID;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }
    }
}
