package kz.alash.naklei.service.esb.dto.moderation;

import java.io.Serializable;
import java.math.BigDecimal;

public class ModerationRequest implements Serializable {

    private String purposeId;
    private String moderationID;
    private String moderationType;
    private String tachometerPhoto;
    private String[] photos;
    private BigDecimal sum;

    public BigDecimal getSum() {
        return sum;
    }

    public String getModerationType() {
        return moderationType;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public String getModerationID() {
        return moderationID;
    }

    public String getTachometerPhoto() {
        return tachometerPhoto;
    }

    public String[] getPhotos() {
        return photos;
    }
}
