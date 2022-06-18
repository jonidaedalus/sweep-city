package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import kz.alash.naklei.entity.dict.EDriverStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "NAKLEI_DRIVER")
@Entity(name = "naklei_Driver")
@NamePattern("%s(%s)|user,car")
public class Driver extends StandardEntity {
    private static final long serialVersionUID = 3373197579132695043L;

    @NotNull
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private ExtUser user;

    @OneToMany(mappedBy = "driver")
    private List<AdvertisementDriver> advertisementDrivers;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAR_ID")
    @NotNull
    private Car car;

    @Column(name = "TOTAL_RUN", columnDefinition = "double precision default 0.0")
    private Double totalRun;

    @Column(name = "WITHDRAWN_MONEY")
    private BigDecimal withdrawnMoney = BigDecimal.ZERO;

    @Column(name = "EARNED_MONEY")
    private BigDecimal earnedMoney;

    @Column(name = "CURRENT_MONEY")
    private BigDecimal currentMoney;

    @Column(name = "DRIVER_STATUS")
    private String driverStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TECH_PASSPORT_DOC_ID")
    private FileDescriptor techPassportDoc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_WITHOUT_STICKER_ID")
    private FileDescriptor photoWithoutSticker;

    @Column(name = "CREDIT_CARD_NAME")
    private String creditCardName;

    @Column(name = "CREDIT_CARD_NUMBER")
    private String creditCardNumber;

    public void setCurrentMoney(BigDecimal currentMoney) {
        this.currentMoney = currentMoney;
    }

    public BigDecimal getCurrentMoney() {
        if (currentMoney == null)
            currentMoney = BigDecimal.ZERO;
        return currentMoney;
    }

    public void setEarnedMoney(BigDecimal earnedMoney) {
        this.earnedMoney = earnedMoney;
    }

    public BigDecimal getEarnedMoney() {
        if (earnedMoney == null)
            earnedMoney = BigDecimal.ZERO;
        return earnedMoney;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public void setTotalRun(Double totalRun) {
        this.totalRun = totalRun;
    }

    public Double getTotalRun() {
        if (totalRun == null)
            totalRun = 0.0;
        return totalRun;
    }

    public List<AdvertisementDriver> getAdvertisementDrivers() {
        return advertisementDrivers;
    }

    public void setAdvertisementDrivers(List<AdvertisementDriver> advertisementDrivers) {
        this.advertisementDrivers = advertisementDrivers;
    }

    public FileDescriptor getPhotoWithoutSticker() {
        return photoWithoutSticker;
    }

    public void setPhotoWithoutSticker(FileDescriptor photoWithoutSticker) {
        this.photoWithoutSticker = photoWithoutSticker;
    }

    public FileDescriptor getTechPassportDoc() {
        return techPassportDoc;
    }

    public void setTechPassportDoc(FileDescriptor techPassportDoc) {
        this.techPassportDoc = techPassportDoc;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public EDriverStatus getDriverStatus() {
        return driverStatus == null ? null : EDriverStatus.fromId(driverStatus);
    }

    public void setDriverStatus(EDriverStatus driverStatus) {
        this.driverStatus = driverStatus == null ? null : driverStatus.getId();
    }

    public BigDecimal getWithdrawnMoney() {
        return withdrawnMoney;
    }

    public void setWithdrawnMoney(BigDecimal withdrawnMoney) {
        this.withdrawnMoney = withdrawnMoney;
    }

    public ExtUser getUser() {
        return user;
    }

    public void setUser(ExtUser user) {
        this.user = user;
    }
}