package kz.alash.naklei.service.esb.dto.visit;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.io.Serializable;

public class CreateVisitResponse extends GenericResponse<CreateVisitResponse.Result> {

    public static class Result implements Serializable {
        private String visitId;

        public String getVisitId() {
            return visitId;
        }

        public void setVisitId(String visitId) {
            this.visitId = visitId;
        }
    }
}
