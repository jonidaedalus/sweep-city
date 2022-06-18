package kz.alash.naklei.service.esb.dto.visit;

import java.io.Serializable;
import java.util.Date;

public class CreateVisitRequest implements Serializable {

//    private String visitType = "STICK";
    private Date visitStart;

//    public String getPurposeId() {
//        return purposeId;
//    }
//
//    public void setPurposeId(String purposeId) {
//        this.purposeId = purposeId;
//    }

    public Date getVisitStart() {
        return visitStart;
    }

    public void setVisitStart(Date visitStart) {
        this.visitStart = visitStart;
    }

//    public String getVisitType() {
//        return visitType;
//    }
//
//    public void setVisitType(String visitType) {
//        this.visitType = visitType;
//    }

}
