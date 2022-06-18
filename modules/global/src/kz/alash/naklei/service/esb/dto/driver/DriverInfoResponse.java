package kz.alash.naklei.service.esb.dto.driver;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;
import java.math.BigDecimal;

public class DriverInfoResponse extends GenericResponse<DriverInfoResponse.Result> {

    public static class Result implements Serializable {
        private String phoneNumber;
        private BigDecimal balance;
        private String status;
        private String carNumber;
        private Double totalRun;

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public void setTotalRun(Double totalRun) {
            this.totalRun = totalRun;
        }
    }


}
