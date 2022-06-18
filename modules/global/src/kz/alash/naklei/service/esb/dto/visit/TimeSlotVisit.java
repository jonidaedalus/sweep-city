package kz.alash.naklei.service.esb.dto.visit;

import java.io.Serializable;

public class TimeSlotVisit implements Serializable {

    private String startTime;
    private String endTime;
    private boolean isFree;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
