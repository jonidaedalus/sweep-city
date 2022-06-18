package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import kz.alash.naklei.entity.dict.car.DClass;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "NAKLEI_D_POINT_COST")
@Entity(name = "naklei_DPointCost")
@NamePattern("%s|id")
public class DPointCost extends BaseUuidEntity {
    private static final long serialVersionUID = -3815635193348189521L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STICKER_TYPE_ID")
    private DStickerType stickerType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAR_CLASS_ID")
    private DClass carClass;

    @NotNull
    @Column(name = "DRIVER_COST", nullable = false)
    private BigDecimal driverCost;

    @NotNull
    @Column(name = "ADVERTISER_COST", nullable = false)
    private BigDecimal advertiserCost;

    public BigDecimal getAdvertiserCost() {
        return advertiserCost;
    }

    public void setAdvertiserCost(BigDecimal advertiserCost) {
        this.advertiserCost = advertiserCost;
    }

    public BigDecimal getDriverCost() {
        return driverCost;
    }

    public void setDriverCost(BigDecimal driverCost) {
        this.driverCost = driverCost;
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