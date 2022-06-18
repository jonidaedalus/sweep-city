package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import kz.alash.naklei.entity.dict.EPayOutStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "NAKLEI_PAY_OUT")
@Entity(name = "naklei_PayOut")
@NamePattern("- %s|sum")
public class PayOut extends StandardEntity {
    private static final long serialVersionUID = -1478825636980790962L;

    @Column(name = "SUM_", nullable = false)
    @NotNull
    private BigDecimal sum;

    @Column(name = "PERCENT_", nullable = false)
    @NotNull
    private Double percent;

    @Column(name = "STATUS", nullable = false)
    @NotNull
    private String status;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID")
    private AdvertisementDriver advertisementDriver;

    public void setStatus(EPayOutStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public EPayOutStatus getStatus() {
        return status == null ? null : EPayOutStatus.fromId(status);
    }

    public AdvertisementDriver getAdvertisementDriver() {
        return advertisementDriver;
    }

    public void setAdvertisementDriver(AdvertisementDriver advertisementDriver) {
        this.advertisementDriver = advertisementDriver;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getPercent() {
        return percent;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getSum() {
        return sum;
    }

}