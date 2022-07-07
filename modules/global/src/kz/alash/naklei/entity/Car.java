package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "NAKLEI_CAR")
@Entity(name = "naklei_Car")
@NamePattern("%s|number, model")
public class Car extends StandardEntity {
    private static final long serialVersionUID = 7824907872780958784L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MODEL_ID")
    private DModel model;

    @NotNull
    @Column(name = "YEAR_", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "NUMBER_", nullable = false)
    private String number;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAR_CLASS_ID")
    private DClass carClass;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COLOR_ID")
    private DColor color;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "car", optional = false)
    @NotNull
    private Driver driver;

    @ManyToMany
    @JoinTable(name = "NAKLEI_CAR_FILE_DESCRIPTOR_LINK",
            joinColumns = @JoinColumn(name = "CAR_ID"),
            inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID"))
    private List<FileDescriptor> photos;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TECH_PASSPORT_ID")
    private FileDescriptor techPassport;

    public void setPhotos(List<FileDescriptor> photos) {
        this.photos = photos;
    }

    public List<FileDescriptor> getPhotos() {
        return photos;
    }

    public FileDescriptor getTechPassport() {
        return techPassport;
    }

    public void setTechPassport(FileDescriptor techPassport) {
        this.techPassport = techPassport;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public DColor getColor() {
        return color;
    }

    public void setColor(DColor color) {
        this.color = color;
    }

    public DClass getCarClass() {
        return carClass;
    }

    public void setCarClass(DClass carClass) {
        this.carClass = carClass;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public DModel getModel() {
        return model;
    }

    public void setModel(DModel model) {
        this.model = model;
    }

}