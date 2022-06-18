package kz.alash.naklei.service.esb.dto.moderation;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;

public class ModerationResponse extends GenericResponse<ModerationResponse.Result> {

    public static class Result implements Serializable {
        private String moderationId;

        public String getModerationId() {
            return moderationId;
        }
        public void setModerationId(String moderationId) {
            this.moderationId = moderationId;
        }
    }
}
