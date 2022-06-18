package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import kz.alash.naklei.entity.dict.DStickerType;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "NAKLEI_ADV_PURPOSE")
@Entity(name = "naklei_AdvPurpose")
@NamePattern("%s,%s|stickerType,carClass")
public class AdvPurpose extends StandardEntity {
    private static final long serialVersionUID = -8906841575332613614L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STIKER_TYPE_ID")
    @NotNull
    private DStickerType stickerType;

    @OneToMany(mappedBy = "purpose")
    private List<AdvertisementDriver> advertisementDrivers;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "PASTING_DATE", nullable = false)
    private Date pastingDate;

    @Column(name = "REWARD_AMOUNT")
    private BigDecimal rewardAmount;

    @Column(name = "PASTING_TIME", nullable = false)
    @NotNull
    private Integer pastingTime;

    @NotNull
    @Column(name = "PASTING_COST", nullable = false)
    private BigDecimal pastingCost;

    @Column(name = "CURRENT_BUDGET", nullable = false)
    @NotNull
    private BigDecimal currentBudget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAR_CLASS_ID")
    @NotNull
    private DClass carClass;

    @ManyToMany
    @JoinTable(name = "NAKLEI_ADV_PURPOSE_D_MODEL_LINK",
            joinColumns = @JoinColumn(name = "ADV_PURPOSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "D_MODEL_ID"))
    private List<DModel> carModel;

    @ManyToMany
    @JoinTable(name = "NAKLEI_ADV_PURPOSE_D_COLOR_LINK",
            joinColumns = @JoinColumn(name = "ADV_PURPOSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "D_COLOR_ID"))
    private List<DColor> carColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STIKER_ID")
    private FileDescriptor sticker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAKET_ID")
    private FileDescriptor maket;

    @NotNull
    @Column(name = "CAR_AMOUNT", nullable = false)
    private Integer carAmount;

    @Column(name = "BUDGET", nullable = false)
    @NotNull
    private BigDecimal budget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADVERTISEMENT_ID")
    @NotNull
    private Advertisement advertisement;

    public List<AdvertisementDriver> getAdvertisementDrivers() {
        return advertisementDrivers;
    }

    public void setAdvertisementDrivers(List<AdvertisementDriver> advertisementDrivers) {
        this.advertisementDrivers = advertisementDrivers;
    }

    public FileDescriptor getMaket() {
        return maket;
    }

    public void setMaket(FileDescriptor maket) {
        this.maket = maket;
    }

    public Date getPastingDate() {
        return pastingDate;
    }

    public void setPastingDate(Date pastingDate) {
        this.pastingDate = pastingDate;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public void setCurrentBudget(BigDecimal currentBudget) {
        this.currentBudget = currentBudget;
    }

    public BigDecimal getCurrentBudget() {
        return currentBudget;
    }

    public BigDecimal getPastingCost() {
        return pastingCost;
    }

    public void setPastingCost(BigDecimal pastingCost) {
        this.pastingCost = pastingCost;
    }

    public Integer getPastingTime() {
        return pastingTime;
    }

    public void setPastingTime(Integer pastingTime) {
        this.pastingTime = pastingTime;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setCarColor(List<DColor> carColor) {
        this.carColor = carColor;
    }

    public List<DColor> getCarColor() {
        return carColor;
    }

    public void setCarModel(List<DModel> carModel) {
        this.carModel = carModel;
    }

    public List<DModel> getCarModel() {
        return carModel;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Integer getCarAmount() {
        return carAmount;
    }

    public void setCarAmount(Integer carAmount) {
        this.carAmount = carAmount;
    }

    public FileDescriptor getSticker() {
        return sticker;
    }

    public void setSticker(FileDescriptor sticker) {
        this.sticker = sticker;
    }

    public DClass getCarClass() {
        return carClass;
    }

    public void setCarClass(DClass carClass) {
        this.carClass = carClass;
    }

    public DStickerType getStickerType() {
        return stickerType;
    }

    public void setStickerType(DStickerType stickerType) {
        this.stickerType = stickerType;
    }
}