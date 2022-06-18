package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.entity.visit.Visit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_NOTIFICATION")
@Entity(name = "naklei_Notification")
@NamePattern("%s|title")
public class Notification extends StandardEntity {
    private static final long serialVersionUID = 8918885480709031830L;

    @JoinColumn(name = "DRIVER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODERATION_ID")
    private Moderation moderation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VISIT_ID")
    private Visit visit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVERTISMENT_ID")
    private Advertisement advertisment;

    @Column(name = "MESSAGE", nullable = false)
    @NotNull
    private String message;

    @Column(name = "TITLE", nullable = false)
    @NotNull
    private String title;

    @NotNull
    @Column(name = "SUCCESS", nullable = false)
    private Boolean success = false;

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Moderation getModeration() {
        return moderation;
    }

    public void setModeration(Moderation moderation) {
        this.moderation = moderation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Advertisement getAdvertisment() {
        return advertisment;
    }

    public void setAdvertisment(Advertisement advertisment) {
        this.advertisment = advertisment;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

}