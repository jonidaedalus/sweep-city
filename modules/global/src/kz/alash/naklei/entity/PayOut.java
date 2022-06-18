package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
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

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID")
    private AdvertisementDriver advertisementDriver;

    public AdvertisementDriver getAdvertisementDriver() {
        return advertisementDriver;
    }

    public void setAdvertisementDriver(AdvertisementDriver advertisementDriver) {
        this.advertisementDriver = advertisementDriver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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