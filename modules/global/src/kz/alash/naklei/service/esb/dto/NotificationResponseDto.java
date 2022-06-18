package kz.alash.naklei.service.esb.dto;

import java.io.Serializable;
import java.util.List;

public class NotificationResponseDto extends GenericResponse<List<NotificationResponseDto.Result>> {
    public static class Result implements Serializable {
        private String title;
        private String message;
        private boolean success;
        private String date;
        private String advertisementId;
        private String moderationId;
        private String visitId;
        private boolean isVerifyModeration;

        public boolean isVerifyModeration() {
            return isVerifyModeration;
        }

        public void setVerifyModeration(boolean verifyModeration) {
            isVerifyModeration = verifyModeration;
        }

        public String getAdvertisementId() {
            return advertisementId;
        }

        public void setAdvertisementId(String advertisementId) {
            this.advertisementId = advertisementId;
        }

        public String getModerationId() {
            return moderationId;
        }

        public void setModerationId(String moderationId) {
            this.moderationId = moderationId;
        }

        public String getVisitId() {
            return visitId;
        }

        public void setVisitId(String visitId) {
            this.visitId = visitId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
