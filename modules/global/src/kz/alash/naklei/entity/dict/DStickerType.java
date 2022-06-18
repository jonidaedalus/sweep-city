package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_D_STICKER_TYPE")
@Entity(name = "naklei_DStickerType")
@NamePattern("%s|name")
public class DStickerType extends BaseUuidEntity {
    private static final long serialVersionUID = 7848688013121362719L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    //todo notnull
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