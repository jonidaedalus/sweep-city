package kz.alash.naklei.service.esb.dto.driver;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;

public class SetAdvertisementResponse extends GenericResponse<SetAdvertisementResponse.Result> {
    public static class Result implements Serializable {
        private String advertisementID;
        private String carNumber;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdvertisementID() {
            return advertisementID;
        }

        public void setAdvertisementID(String advertisementID) {
            this.advertisementID = advertisementID;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }
    }
}
