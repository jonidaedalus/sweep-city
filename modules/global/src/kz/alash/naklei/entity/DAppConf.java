package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_D_APP_CONF")
@Entity(name = "naklei_DAppConf")
@NamePattern("%s|transactionPercent")
public class DAppConf extends StandardEntity {
    private static final long serialVersionUID = 585339120073712788L;

    @Column(name = "TRAN_PERCENT_", nullable = false)
    @NotNull
    private Double transactionPercent;

    @NotNull
    @Column(name = "DAYS", nullable = false)
    private Integer days;

    public void setTransactionPercent(Double transactionPercent) {
        this.transactionPercent = transactionPercent;
    }

    public Double getTransactionPercent() {
        return transactionPercent;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

}