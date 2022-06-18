package kz.alash.naklei.service.esb.dto.firebase;

import java.io.Serializable;

public class PushRequest implements Serializable {
    private String to;
    private Notification notification;
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public static class Notification implements Serializable {
        private String title;
        private String body;
        private String sound;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }
    }

    public static class Data implements Serializable {
        private String driverId;
        private String advertisementId;
        private String moderationId;

        private String visitId;

        public String getVisitId() {
            return visitId;
        }

        public void setVisitId(String visitId) {
            this.visitId = visitId;
        }

        public String getDriverId() {
            return driverId;
        }

        public void setDriverId(String driverId) {
            this.driverId = driverId;
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
    }
}
