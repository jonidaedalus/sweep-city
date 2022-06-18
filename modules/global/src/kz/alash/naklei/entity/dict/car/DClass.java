package kz.alash.naklei.entity.dict.car;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_D_CLASS")
@Entity(name = "naklei_DClass")
@NamePattern("%s|name")
public class DClass extends BaseUuidEntity {
    private static final long serialVersionUID = 6256263000947916148L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "COVERAGE_COEF")
    private Double coverageCoef;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    //todo notnull
    @Column(name = "POINT_COEF", nullable = false)
    @NotNull
    private Double pointCoef;

    @Column(name = "MAX_POINT_PER_DAY", nullable = false)
    @NotNull
    private Integer maxPointPerDay;

    public Double getCoverageCoef() {
        return coverageCoef;
    }

    public void setCoverageCoef(Double coverageCoef) {
        this.coverageCoef = coverageCoef;
    }

    public Integer getMaxPointPerDay() {
        return maxPointPerDay;
    }

    public void setMaxPointPerDay(Integer maxPointPerDay) {
        this.maxPointPerDay = maxPointPerDay;
    }

    public Double getPointCoef() {
        return pointCoef;
    }

    public void setPointCoef(Double pointCoef) {
        this.pointCoef = pointCoef;
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