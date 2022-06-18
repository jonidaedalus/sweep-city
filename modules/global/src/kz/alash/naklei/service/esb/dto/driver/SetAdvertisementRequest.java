package kz.alash.naklei.service.esb.dto.driver;

import java.io.Serializable;

public class SetAdvertisementRequest implements Serializable {
    private String advertisementID;
    private String carNumber;

    public String getAdvertisementID() {
        return advertisementID;
    }

    public String getCarNumber() {
        return carNumber;
    }
}
