package kz.alash.naklei.service.esb.dto.advertisement;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdvertisementDriverDto implements Serializable {
    private String name;
    private String activityName;
    private String dateString; //dd1.MM1.yyyy1 - dd2.MM2.yyyy2
    private BigDecimal averagePoints;
    private Integer averageDistance;
    private Integer averageTimes;
    private Integer totalPoints;
    private Integer totalDistance;
    private Integer totalTimes;

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

    public BigDecimal getAveragePoints() {
        return averagePoints;
    }

    public void setAveragePoints(BigDecimal averagePoints) {
        this.averagePoints = averagePoints;
    }

    public Integer getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(Integer averageDistance) {
        this.averageDistance = averageDistance;
    }

    public Integer getAverageTimes() {
        return averageTimes;
    }

    public void setAverageTimes(Integer averageTimes) {
        this.averageTimes = averageTimes;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }
}
