package kz.alash.naklei.entity.visit;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.dict.EVisitCancelReason;
import kz.alash.naklei.entity.dict.EVisitStatus;
import kz.alash.naklei.entity.dict.EVisitType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Table(name = "NAKLEI_VISIT")
@Entity(name = "naklei_Visit")
@NamePattern("%s|advertisementDriver")
public class Visit extends StandardEntity {
    private static final long serialVersionUID = -4150731968254214555L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ASSIGNED_USER_ID")
    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    private User assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private VisitCompany company;

    @Column(name = "REVISITED")
    private Boolean revisited;

    @Column(name = "CANCEL_REASON")
    private String cancelReason;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "COMMENT_")
    private String comment;

    @Column(name = "TYPE_", nullable = false)
    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID")
    @NotNull
    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    private AdvertisementDriver advertisementDriver;

    @Column(name = "VISIT_START")
    private LocalDateTime visitStart;

    @Column(name = "VISIT_END")
    private LocalDateTime visitEnd;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    public VisitCompany getCompany() {
        return company;
    }

    public void setCompany(VisitCompany company) {
        this.company = company;
    }

    public Boolean getRevisited() {
        return revisited;
    }

    public void setRevisited(Boolean revisited) {
        this.revisited = revisited;
    }

    public EVisitCancelReason getCancelReason() {
        return cancelReason == null ? null : EVisitCancelReason.fromId(cancelReason);
    }

    public void setCancelReason(EVisitCancelReason cancelReason) {
        this.cancelReason = cancelReason == null ? null : cancelReason.getId();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EVisitStatus getStatus() {
        return status == null ? null : EVisitStatus.fromId(status);
    }

    public void setStatus(EVisitStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @MetaProperty(related = "advertisementDriver")
    public String getDriverName() {
        if (advertisementDriver != null)
            return advertisementDriver.getDriver().getUser().getPhoneNumber();
        return null;
    }

    @MetaProperty(related = "type")
    public String getTypeStyle() {
        return Optional.ofNullable(getType())
                .map(EVisitType::getStyleName)
                .orElse("");
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public LocalDateTime getVisitEnd() {
        return visitEnd;
    }

    public void setVisitEnd(LocalDateTime visitEnd) {
        this.visitEnd = visitEnd;
    }

    public LocalDateTime getVisitStart() {
        return visitStart;
    }

    public void setVisitStart(LocalDateTime visitStart) {
        this.visitStart = visitStart;
    }

    public AdvertisementDriver getAdvertisementDriver() {
        return advertisementDriver;
    }

    public void setAdvertisementDriver(AdvertisementDriver advertisementDriver) {
        this.advertisementDriver = advertisementDriver;
    }

    public EVisitType getType() {
        return type == null ? null : EVisitType.fromId(type);
    }

    public void setType(EVisitType type) {
        this.type = type == null ? null : type.getId();
    }
}