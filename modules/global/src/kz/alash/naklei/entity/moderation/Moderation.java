package kz.alash.naklei.entity.moderation;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.dict.EModerationStatus;
import kz.alash.naklei.entity.dict.EModerationType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "NAKLEI_MODERATION")
@Entity(name = "naklei_Moderation")
public class Moderation extends StandardEntity {
    private static final long serialVersionUID = -7633095848126565820L;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "SUM_")
    private BigDecimal sum;

    @Column(name = "REASON")
    private String reason;

    @JoinTable(name = "NAKLEI_MODERATION_FILE_DESCRIPTOR_LINK",
            joinColumns = @JoinColumn(name = "MODERATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID"))
    @ManyToMany
    @OnDelete(DeletePolicy.CASCADE)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    private List<FileDescriptor> photos;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID")
    @OnDelete(DeletePolicy.UNLINK)
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private AdvertisementDriver advertisementDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODERATOR_ID")
    @OnDelete(DeletePolicy.UNLINK)
    @OnDeleteInverse(DeletePolicy.DENY)
    private User moderator;

    @Lob
    @Column(name = "MESSAGE")
    private String message;

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public User getModerator() {
        return moderator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AdvertisementDriver getAdvertisementDriver() {
        return advertisementDriver;
    }

    public void setAdvertisementDriver(AdvertisementDriver advertisementDriver) {
        this.advertisementDriver = advertisementDriver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileDescriptor> getPhotos() {
        return photos;
    }

    public void setPhotos(List<FileDescriptor> photos) {
        this.photos = photos;
    }


    public EModerationStatus getStatus() {
        return status == null ? null : EModerationStatus.fromId(status);
    }

    public void setStatus(EModerationStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public EModerationType getType() {
        return type == null ? null : EModerationType.fromId(type);
    }

    public void setType(EModerationType type) {
        this.type = type == null ? null : type.getId();
    }
}