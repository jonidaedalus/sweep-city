package kz.alash.naklei.service.esb.dto.driver;

import java.io.Serializable;

public class DriverRegistrationRequest implements Serializable {
    private String phoneNumber;
    private String password;
    private String appleID;
    private String fullName;
    private String cityCode;
    private String carModelCode;
    private int carYear;
    private String carNumber;
    private String carColorCode;
    private String technicalPassportPhoto;
    private String[] carPhoto;
    private String coveredDistance;

    public String getAppleID() {
        return appleID;
    }

    public void setAppleID(String appleID) {
        this.appleID = appleID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCarModelCode() {
        return carModelCode;
    }

    public void setCarModelCode(String carModelCode) {
        this.carModelCode = carModelCode;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarColorCode() {
        return carColorCode;
    }

    public void setCarColorCode(String carColorCode) {
        this.carColorCode = carColorCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTechnicalPassportPhoto() {
        return technicalPassportPhoto;
    }

    public void setTechnicalPassportPhoto(String technicalPassportPhoto) {
        this.technicalPassportPhoto = technicalPassportPhoto;
    }

    public String[] getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String[] carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getCoveredDistance() {
        return coveredDistance;
    }

    public void setCoveredDistance(String coveredDistance) {
        this.coveredDistance = coveredDistance;
    }
}
