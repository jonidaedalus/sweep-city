package kz.alash.naklei.entity.dict;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "NAKLEI_D_TRAFIC")
@Entity(name = "naklei_DTrafic")
public class DTraffic extends StandardEntity {
    private static final long serialVersionUID = -4448255371408686905L;

    @JoinColumn(name = "ZONE_TYPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private DZoneType zoneType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ADVETISMENT_ID")
//    private Advertisement advetisment;

    @Column(name = "HOUR_")
    private Integer hour;

    @Column(name = "HOUR_STRING")
    private String hourString;

    @Column(name = "AVERAGE_SPEED", nullable = false)
    @NotNull
    private BigDecimal averageSpeed;

    @Column(name = "AVERAGE_SPEED_WEEKEND")
    private BigDecimal averageSpeedWeekend;

    @Column(name = "COVERAGE_PER_KM", nullable = false)
    @NotNull
    private BigDecimal coveragePerKm;

    @Column(name = "COVERAGE_PER_KM_WEEKEND", nullable = false)
    @NotNull
    private BigDecimal coveragePerKmWeekend;

    @Column(name = "VALUE_", nullable = false)
    @NotNull
    private Integer value;

    @Column(name = "VALUE_WEEKENDS", nullable = false)
    @NotNull
    private Integer valueWeekends;

    public void setAverageSpeedWeekend(BigDecimal averageSpeedWeekend) {
        this.averageSpeedWeekend = averageSpeedWeekend;
    }

    public BigDecimal getAverageSpeedWeekend() {
        return averageSpeedWeekend;
    }

    public BigDecimal getCoveragePerKmWeekend() {
        return coveragePerKmWeekend;
    }

    public void setCoveragePerKmWeekend(BigDecimal coveragePerKmWeekend) {
        this.coveragePerKmWeekend = coveragePerKmWeekend;
    }

    public BigDecimal getCoveragePerKm() {
        return coveragePerKm;
    }

    public void setCoveragePerKm(BigDecimal coveragePerKm) {
        this.coveragePerKm = coveragePerKm;
    }

    public BigDecimal getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(BigDecimal averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getHourString() {
        return hourString;
    }

    public void setHourString(String hourString) {
        this.hourString = hourString;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getValueWeekends() {
        return valueWeekends;
    }

    public void setValueWeekends(Integer valueWeekends) {
        this.valueWeekends = valueWeekends;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

//    public Advertisement getAdvetisment() {
//        return advetisment;
//    }
//
//    public void setAdvetisment(Advertisement advetisment) {
//        this.advetisment = advetisment;
//    }

    public void setZoneType(DZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public DZoneType getZoneType() {
        return zoneType;
    }

}