package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "NAKLEI_ADVERTISER")
@Entity(name = "naklei_Advertiser")
@NamePattern("%s|fullName")
public class Advertiser extends StandardEntity {
    private static final long serialVersionUID = -6435475749533250135L;

    //todo address, BIN, bank, IIK, KBE (taxes stuff), BIK, phone number, numOfAdvertisements/
    //todo delete balance from advertiser
    //todo add balance for each advertisement
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOGO_ID")
    private FileDescriptor logo;

    @NotNull
    @Column(name = "DESC_", nullable = false)
    private String desc;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "advertiser")
    private List<AdvLocation> locations;

    @NotNull
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "advertiser")
    private List<Advertisement> advertisements;

    @Column(name = "FULL_NAME", nullable = false, length = 75)
    @NotNull
    private String fullName;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<AdvLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<AdvLocation> locations) {
        this.locations = locations;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public FileDescriptor getLogo() {
        return logo;
    }

    public void setLogo(FileDescriptor logo) {
        this.logo = logo;
    }

}