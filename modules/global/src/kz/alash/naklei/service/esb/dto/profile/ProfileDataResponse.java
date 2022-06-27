package kz.alash.naklei.service.esb.dto.profile;

import kz.alash.naklei.service.esb.dto.GenericResponse;
import kz.alash.naklei.service.esb.dto.moderation.ModerationDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProfileDataResponse extends GenericResponse<ProfileDataResponse.Result> {
    public static class Result implements Serializable {
        private CarDto car;
        private String fullName;
        private String cityCode;
        private String cityName;
        private BigDecimal outBalance;
        private BigDecimal currentBalance;
        private String phoneNumber;
        private AdvertisementDto currentAdvertisement;
//        private List<AdvertisementDto> advertisements;
//        private List<ModerationDto> moderations;
        private String creditCardNumber;
        private String creditCardName;
        private Integer allPoints;
        private Integer allDistance;
        private Integer allTime;

        public Integer getAllPoints() {
            return allPoints;
        }

        public void setAllPoints(Integer allPoints) {
            this.allPoints = allPoints;
        }

        public Integer getAllDistance() {
            return allDistance;
        }

        public void setAllDistance(Integer allDistance) {
            this.allDistance = allDistance;
        }

        public Integer getAllTime() {
            return allTime;
        }

        public void setAllTime(Integer allTime) {
            this.allTime = allTime;
        }

        public String getCreditCardNumber() {
            return creditCardNumber;
        }

        public void setCreditCardNumber(String creditCardNumber) {
            this.creditCardNumber = creditCardNumber;
        }

        public String getCreditCardName() {
            return creditCardName;
        }

        public void setCreditCardName(String creditCardName) {
            this.creditCardName = creditCardName;
        }

//        public List<ModerationDto> getModerations() {
//            return moderations;
//        }

//        public void setModerations(List<ModerationDto> moderations) {
//            this.moderations = moderations;
//        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public BigDecimal getOutBalance() {
            return outBalance;
        }

        public void setOutBalance(BigDecimal outBalance) {
            this.outBalance = outBalance;
        }

        public BigDecimal getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
        }

//        public List<AdvertisementDto> getAdvertisements() {
//            return advertisements;
//        }

//        public void setAdvertisements(List<AdvertisementDto> advertisements) {
//            this.advertisements = advertisements;
//        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public CarDto getCar() {
            return car;
        }

        public void setCar(CarDto car) {
            this.car = car;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public AdvertisementDto getCurrentAdvertisement() {
            return currentAdvertisement;
        }

        public void setCurrentAdvertisement(AdvertisementDto currentAdvertisement) {
            this.currentAdvertisement = currentAdvertisement;
        }
    }

    public static class VisitDto implements Serializable {
        private String type;
        private String status;
        private String companyAddressMapUrl;
        private String companyAddress;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String cancelReason;
        private String comment;

        public String getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public String getCompanyAddressMapUrl() {
            return companyAddressMapUrl;
        }

        public void setCompanyAddressMapUrl(String companyAddressMapUrl) {
            this.companyAddressMapUrl = companyAddressMapUrl;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }
    }
    public static class AdvertisementDto implements Serializable {

        private String advertiserId;
        private String purposeId;
        private VisitDto visit;
        private List<ModerationDto> moderations;
        private String name;
        private String activityName;
        private String dateString; //dd1.MM1.yyyy1 - dd2.MM2.yyyy2
        private double averagePoints;
        private double averageDistance;
        private double averageTimes;
        private int totalPoints;
        private double totalDistance;
        private double totalTimes;
        private double todayPoints;

        public double getTodayDistance() {
            return todayDistance;
        }

        public void setTodayDistance(double todayDistance) {
            this.todayDistance = todayDistance;
        }

        private double todayDistance;
        private String status;
        private String stickerTypeName;
        private Integer advertiserPointCost;
        private LocalDateTime pastingDate;

        private String carClass;

        public String getCarClass() {
            return carClass;
        }

        public void setCarClass(String carClass) {
            this.carClass = carClass;
        }

        public LocalDateTime getPastingDate() {
            return pastingDate;
        }

        public void setPastingDate(LocalDateTime pastingDate) {
            this.pastingDate = pastingDate;
        }

        public Integer getAdvertiserPointCost() {
            return advertiserPointCost;
        }

        public void setAdvertiserPointCost(Integer advertiserPointCost) {
            this.advertiserPointCost = advertiserPointCost;
        }

        public String getAdvertiserId() {
            return advertiserId;
        }

        public void setAdvertiserId(String advertiserId) {
            this.advertiserId = advertiserId;
        }

        public String getStickerTypeName() {
            return stickerTypeName;
        }

        public void setStickerTypeName(String stickerTypeName) {
            this.stickerTypeName = stickerTypeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getDateString() {
            return dateString;
        }

        public void setDateString(String dateString) {
            this.dateString = dateString;
        }

        public double getAveragePoints() {
            return averagePoints;
        }

        public void setAveragePoints(double averagePoints) {
            this.averagePoints = averagePoints;
        }

        public double getAverageDistance() {
            return averageDistance;
        }

        public void setAverageDistance(double averageDistance) {
            this.averageDistance = averageDistance;
        }

        public double getAverageTimes() {
            return averageTimes;
        }

        public void setAverageTimes(double averageTimes) {
            this.averageTimes = averageTimes;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(int totalPoints) {
            this.totalPoints = totalPoints;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public void setTotalDistance(double totalDistance) {
            this.totalDistance = totalDistance;
        }

        public double getTotalTimes() {
            return totalTimes;
        }

        public void setTotalTimes(double totalTimes) {
            this.totalTimes = totalTimes;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public VisitDto getVisit() {
            return visit;
        }

        public void setVisit(VisitDto visit) {
            this.visit = visit;
        }

        public List<ModerationDto> getModerations() {
            if (moderations == null)
                moderations = new ArrayList<>();
            return moderations;
        }

        public void setModerations(List<ModerationDto> moderations) {
            this.moderations = moderations;
        }

        public double getTodayPoints() {
            return todayPoints;
        }

        public void setTodayPoints(double todayPoints) {
            this.todayPoints = todayPoints;
        }

        public String getPurposeId() {
            return purposeId;
        }

        public void setPurposeId(String purposeId) {
            this.purposeId = purposeId;
        }
    }
    public static class CarDto implements Serializable {
        private String number;
        private String photo;
        private String className;
        private String classCode;
        private String modelCode;
        private String modelName;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getModelCode() {
            return modelCode;
        }

        public void setModelCode(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getClassCode() {
            return classCode;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

}
