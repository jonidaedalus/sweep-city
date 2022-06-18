package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EVisitCancelReason implements EnumClass<String> {

    REVISIT("REVISIT"),
    ABSENCE("ABSENCE"),
    OTHER("OTHER");

    private String id;

    EVisitCancelReason(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EVisitCancelReason fromId(String id) {
        for (EVisitCancelReason at : EVisitCancelReason.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}