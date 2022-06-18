package kz.alash.naklei.service.esb.dto.driver;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;

public class DriverRegistrationResponse extends GenericResponse<DriverRegistrationResponse.Result> {

    public static class Result implements Serializable {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


}