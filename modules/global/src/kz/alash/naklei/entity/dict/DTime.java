package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Table(name = "NAKLEI_D_TIME")
@Entity(name = "naklei_DTime")
@NamePattern("%s|name")
public class DTime extends StandardEntity {
    private static final long serialVersionUID = 3207534666631264668L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "TIME_FROM")
    private LocalTime timeFrom;

    @Column(name = "TIME_TO")
    private LocalTime timeTo;

    //todo notnoll
    @Column(name = "POINT_COEF")
    private Double pointCoef;

    @Column(name = "COVERAGE_COEF")
    private Double coverageCoef;

    public Double getCoverageCoef() {
        return coverageCoef;
    }

    public void setCoverageCoef(Double coverageCoef) {
        this.coverageCoef = coverageCoef;
    }

    public Double getPointCoef() {
        return pointCoef;
    }

    public void setPointCoef(Double pointCoef) {
        this.pointCoef = pointCoef;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
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