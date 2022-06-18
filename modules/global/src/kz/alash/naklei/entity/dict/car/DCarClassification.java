package kz.alash.naklei.entity.dict.car;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//Классикация машин
@Table(name = "NAKLEI_D_CAR_CLASSIFICATION")
@Entity(name = "naklei_DCarClassification")
@NamePattern("%s|classs")
public class DCarClassification extends BaseUuidEntity {
    private static final long serialVersionUID = 5478848210996760105L;

    //Класс машины
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLASSS_ID")
    private DClass classs;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MARK_ID")
    @NotNull
    private DModel model;

    @Column(name = "FIRST_YEAR")
    private Integer firstYear;

    @Column(name = "LAST_YEAR")
    private Integer lastYear;

    public Integer getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(Integer firstYear) {
        this.firstYear = firstYear;
    }

    public Integer getLastYear() {
        return lastYear;
    }

    public void setLastYear(Integer lastYear) {
        this.lastYear = lastYear;
    }

    public void setModel(DModel model) {
        this.model = model;
    }

    public DModel getModel() {
        return model;
    }

    public DClass getClasss() {
        return classs;
    }

    public void setClasss(DClass classs) {
        this.classs = classs;
    }
}