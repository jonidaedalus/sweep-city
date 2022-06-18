package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.EAdvStatus;
import org.eclipse.persistence.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "NAKLEI_ADVERTISEMENT")
@Entity(name = "naklei_Advertisement")
@NamePattern("%s|advertiser")
public class Advertisement extends StandardEntity {
    private static final long serialVersionUID = 7076235734367364493L;

    public static final int DAYS_PER_MONTH = 30;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADVERTISER_ID")
    private Advertiser advertiser;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "advertisement")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<AdvPurpose> purposes;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", nullable = false)
    @NotNull
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CITY_ID")
    @NotNull
    private DCity city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdvPurpose> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<AdvPurpose> purposes) {
        this.purposes = purposes;
    }

    public EAdvStatus getStatus() {
        return status == null ? null : EAdvStatus.fromId(status);
    }

    public void setStatus(EAdvStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public DCity getCity() {
        return city;
    }

    public void setCity(DCity city) {
        this.city = city;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }
}