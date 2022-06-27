package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "NAKLEI_ADVERTISEMENT_DRIVER")
@Entity(name = "naklei_AdvertisementDriver")
@NamePattern("%s|driver")
public class AdvertisementDriver extends StandardEntity {
    private static final long serialVersionUID = 8434657602411019555L;

    @JoinColumn(name = "DRIVER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @Column(name = "IS_STICKED")
    private Boolean isSticked;

    @Column(name = "STICKED_WITHIN_PERIOD")
    private Boolean stickedWithinPeriod;

    @Column(name = "EARNED_MONEY")
    private BigDecimal earnedMoney = BigDecimal.ZERO;

    @Column(name = "CURRENT_MONEY")
    private BigDecimal currentMoney = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURPOSE_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private AdvPurpose purpose;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIAL_TACHOMETER_PHOTO_ID")
    private FileDescriptor initialTachometerPhoto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINAL_TACHOMETER_PHOTO_ID")
    private FileDescriptor finalTachometerPhoto;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "TOTAL_RUN")
    private Double totalRun;

    @Column(name = "APPROVED_TOTAL_RUN")
    private Double approvedTotalRun;

    @OneToMany(mappedBy = "advertisementDriver")
    private List<Route> routes;

    @Column(name = "TACHOMETER_VALUE")
    private Double tachometerValue;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_WASH_DATE")
    private Date lastWashDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_MODERATION_DATE")
    private Date lastModerationDate;

    @JoinTable(name = "NAKLEI_ADVERTISEMENT_DRIVER_FILE_DESCRIPTOR_LINK",
            joinColumns = @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID"))
    @ManyToMany
    private List<FileDescriptor> photos;

    public Boolean getStickedWithinPeriod() {
        return stickedWithinPeriod;
    }

    public void setStickedWithinPeriod(Boolean stickedWithinPeriod) {
        this.stickedWithinPeriod = stickedWithinPeriod;
    }

    public Boolean getIsSticked() {
        return isSticked;
    }

    public void setIsSticked(Boolean isSticked) {
        this.isSticked = isSticked;
    }

    public void setTotalRun(Double totalRun) {
        this.totalRun = totalRun;
    }

    public Double getTotalRun() {
        if (totalRun == null)
            totalRun = 0.0;
        return totalRun;
    }

    public void setApprovedTotalRun(Double approvedTotalRun) {
        this.approvedTotalRun = approvedTotalRun;
    }

    public Double getApprovedTotalRun() {
        return approvedTotalRun;
    }

    public void setTachometerValue(Double tachometerValue) {
        this.tachometerValue = tachometerValue;
    }

    public Double getTachometerValue() {
        return tachometerValue;
    }

    public void setEarnedMoney(BigDecimal earnedMoney) {
        this.earnedMoney = earnedMoney;
    }

    public BigDecimal getEarnedMoney() {
        if (earnedMoney == null)
            earnedMoney = BigDecimal.ZERO;
        return earnedMoney;
    }

    public BigDecimal getCurrentMoney() {
        if (currentMoney == null)
            currentMoney = BigDecimal.ZERO;
        return currentMoney;
    }

    public void setCurrentMoney(BigDecimal currentMoney) {
        this.currentMoney = currentMoney;
    }

    public AdvPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(AdvPurpose purpose) {
        this.purpose = purpose;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public FileDescriptor getFinalTachometerPhoto() {
        return finalTachometerPhoto;
    }

    public void setFinalTachometerPhoto(FileDescriptor finalTachometerPhoto) {
        this.finalTachometerPhoto = finalTachometerPhoto;
    }

    public FileDescriptor getInitialTachometerPhoto() {
        return initialTachometerPhoto;
    }

    public void setInitialTachometerPhoto(FileDescriptor initialTachometerPhoto) {
        this.initialTachometerPhoto = initialTachometerPhoto;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EAdvDriverStatus getStatus() {
        return status == null ? null : EAdvDriverStatus.fromId(status);
    }

    public void setStatus(EAdvDriverStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public List<FileDescriptor> getPhotos() {
        return photos;
    }

    public void setPhotos(List<FileDescriptor> photos) {
        this.photos = photos;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public Date getLastModerationDate() {
        return lastModerationDate;
    }

    public void setLastModerationDate(Date lastModerationDate) {
        this.lastModerationDate = lastModerationDate;
    }

    public Date getLastWashDate() {
        return lastWashDate;
    }

    public void setLastWashDate(Date lastWashDate) {
        this.lastWashDate = lastWashDate;
    }

}