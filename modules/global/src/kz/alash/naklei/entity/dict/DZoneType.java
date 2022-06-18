package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "NAKLEI_D_ZONE_TYPE")
@Entity(name = "naklei_DZoneType")
@NamePattern("%s|name")
public class DZoneType extends StandardEntity {
    private static final long serialVersionUID = 7899053623398129270L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "COVERAGE_WEEKEND", nullable = false)
    private BigDecimal coverageWeekend;

    @NotNull
    @Column(name = "POINT_COEF", nullable = false)
    private Double pointCoef;

    @OneToMany(mappedBy = "zoneType")
    private List<DTraffic> traffics;

    @Column(name = "COVERAGE", nullable = false)
    @NotNull
    private BigDecimal coverage;

    @Column(name = "COVERAGE_PER_HOUR", nullable = false)
    @NotNull
    private BigDecimal coveragePerHour;

    @Column(name = "COVERAGE_PER_HOUR_WEEKEND", nullable = false)
    @NotNull
    private BigDecimal coveragePerHourWeekend;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    private DCity city;

    @Column(name = "COLOR")
    private String color;

    public BigDecimal getCoverageWeekend() {
        return coverageWeekend;
    }

    public void setCoverageWeekend(BigDecimal coverageWeekend) {
        this.coverageWeekend = coverageWeekend;
    }

    public Double getPointCoef() {
        return pointCoef;
    }

    public void setPointCoef(Double pointCoef) {
        this.pointCoef = pointCoef;
    }

    public List<DTraffic> getTraffics() {
        if(traffics != null && traffics.size() > 0)
            return traffics.stream().sorted(Comparator.comparing(DTraffic::getHour)).collect(Collectors.toList());
        else
            return traffics;
    }

    public void setTraffics(List<DTraffic> traffics) {
        this.traffics = traffics;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getCoveragePerHourWeekend() {
        return coveragePerHourWeekend;
    }

    public void setCoveragePerHourWeekend(BigDecimal coveragePerHourWeekend) {
        this.coveragePerHourWeekend = coveragePerHourWeekend;
    }

    public BigDecimal getCoveragePerHour() {
        return coveragePerHour;
    }

    public void setCoveragePerHour(BigDecimal coveragePerHour) {
        this.coveragePerHour = coveragePerHour;
    }

    public BigDecimal getCoverage() {
        return coverage;
    }

    public void setCoverage(BigDecimal coverage) {
        this.coverage = coverage;
    }

    public DCity getCity() {
        return city;
    }

    public void setCity(DCity city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}