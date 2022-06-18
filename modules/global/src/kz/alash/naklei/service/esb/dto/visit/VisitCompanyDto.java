package kz.alash.naklei.service.esb.dto.visit;

import java.io.Serializable;

public class VisitCompanyDto implements Serializable {

    private String name;
    private String mapUrlLink;
    private String address;
    private String hoursFrom;
    private String hoursTo;
    private String satHoursFrom;
    private String satHoursTo;
    private String sunHoursFrom;
    private String sunHoursTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapUrlLink() {
        return mapUrlLink;
    }

    public void setMapUrlLink(String mapUrlLink) {
        this.mapUrlLink = mapUrlLink;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHoursFrom() {
        return hoursFrom;
    }

    public void setHoursFrom(String hoursFrom) {
        this.hoursFrom = hoursFrom;
    }

    public String getHoursTo() {
        return hoursTo;
    }

    public void setHoursTo(String hoursTo) {
        this.hoursTo = hoursTo;
    }

    public String getSatHoursFrom() {
        return satHoursFrom;
    }

    public void setSatHoursFrom(String satHoursFrom) {
        this.satHoursFrom = satHoursFrom;
    }

    public String getSatHoursTo() {
        return satHoursTo;
    }

    public void setSatHoursTo(String satHoursTo) {
        this.satHoursTo = satHoursTo;
    }

    public String getSunHoursFrom() {
        return sunHoursFrom;
    }

    public void setSunHoursFrom(String sunHoursFrom) {
        this.sunHoursFrom = sunHoursFrom;
    }

    public String getSunHoursTo() {
        return sunHoursTo;
    }

    public void setSunHoursTo(String sunHoursTo) {
        this.sunHoursTo = sunHoursTo;
    }

}