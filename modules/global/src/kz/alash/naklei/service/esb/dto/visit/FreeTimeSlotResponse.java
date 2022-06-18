package kz.alash.naklei.service.esb.dto.visit;

import java.io.Serializable;
import java.util.List;

public class FreeTimeSlotResponse implements Serializable {
    private List<TimeSlotVisit> visits;

    public List<TimeSlotVisit> getVisits() {
        return visits;
    }

    public void setVisits(List<TimeSlotVisit> visits) {
        this.visits = visits;
    }
}


