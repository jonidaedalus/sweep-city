package kz.alash.naklei.service.esb.dto.moderation;

import java.io.Serializable;

public class ModerationDto implements Serializable {
    private String moderationId;
    private String moderationStatus;
    private String moderationType;
    private String message;

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModerationId() {
        return moderationId;
    }

    public void setModerationId(String moderationId) {
        this.moderationId = moderationId;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String getModerationType() {
        return moderationType;
    }

    public void setModerationType(String moderationType) {
        this.moderationType = moderationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
