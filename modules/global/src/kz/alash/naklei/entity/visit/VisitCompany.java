package kz.alash.naklei.entity.visit;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.EVisitType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Table(name = "NAKLEI_VISIT_COMPANY")
@Entity(name = "naklei_VisitCompany")
@NamePattern("%s|name")
public class VisitCompany extends StandardEntity {
    private static final long serialVersionUID = 2145797149838769880L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID")
    @NotNull
    private ExtUser employee;

    @NotNull
    @Column(name = "MAP_LINK", nullable = false)
    private String mapLink;

    @NotNull
    @Column(name = "ADDRESS_TEXT", nullable = false)
    private String addressText;

    @NotNull
    @Column(name = "VISIT_TYPE", nullable = false)
    private String visitType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CITY_ID")
    @NotNull
    private DCity city;

    @Column(name = "WORK_FROM", nullable = false)
    @NotNull
    private LocalTime workFrom;

    @Column(name = "WORK_TO", nullable = false)
    @NotNull
    private LocalTime workTo;

    @Column(name = "SAT_WORK_FROM")
    private LocalTime satWorkFrom;

    @Column(name = "SAT_WORK_TO")
    private LocalTime satWorkTo;

    @Column(name = "SUN_WORK_FROM")
    private LocalTime sunWorkFrom;

    @Column(name = "SUN_WORK_TO")
    private LocalTime sunWorkTo;

    public LocalTime getSunWorkTo() {
        return sunWorkTo;
    }

    public void setSunWorkTo(LocalTime sunWorkTo) {
        this.sunWorkTo = sunWorkTo;
    }

    public void setSatWorkTo(LocalTime satWorkTo) {
        this.satWorkTo = satWorkTo;
    }

    public LocalTime getSatWorkTo() {
        return satWorkTo;
    }

    public LocalTime getSunWorkFrom() {
        return sunWorkFrom;
    }

    public void setSunWorkFrom(LocalTime sunWorkFrom) {
        this.sunWorkFrom = sunWorkFrom;
    }

    public LocalTime getSatWorkFrom() {
        return satWorkFrom;
    }

    public void setSatWorkFrom(LocalTime satWorkFrom) {
        this.satWorkFrom = satWorkFrom;
    }

    public void setWorkFrom(LocalTime workFrom) {
        this.workFrom = workFrom;
    }

    public LocalTime getWorkFrom() {
        return workFrom;
    }

    public void setWorkTo(LocalTime workTo) {
        this.workTo = workTo;
    }

    public LocalTime getWorkTo() {
        return workTo;
    }

    public ExtUser getEmployee() {
        return employee;
    }

    public void setEmployee(ExtUser employee) {
        this.employee = employee;
    }

    public DCity getCity() {
        return city;
    }

    public void setCity(DCity city) {
        this.city = city;
    }

    public EVisitType getVisitType() {
        return visitType == null ? null : EVisitType.fromId(visitType);
    }

    public void setVisitType(EVisitType visitType) {
        this.visitType = visitType == null ? null : visitType.getId();
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}