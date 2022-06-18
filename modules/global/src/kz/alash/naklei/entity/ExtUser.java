package kz.alash.naklei.entity;

import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.dict.DCity;

import javax.persistence.*;

@PublishEntityChangedEvents
@DiscriminatorValue("naklei_ExtUser")
@Entity(name = "naklei_ExtUser")
@Extends(User.class)
public class ExtUser extends User {
    private static final long serialVersionUID = -1489065774934417366L;

    @Column(name = "PHONE_NUMBER", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "APPLE_ID")
    @Lob
    private String appleID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    private DCity city;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Driver driver;

    @Column(name = "FCM_TOKEN")
    private String fcmToken;

    public String getAppleID() {
        return appleID;
    }

    public void setAppleID(String appleID) {
        this.appleID = appleID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public DCity getCity() {
        return city;
    }

    public void setCity(DCity city) {
        this.city = city;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}