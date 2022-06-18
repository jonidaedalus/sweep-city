package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EAdvDriverStatus implements EnumClass<String> {

    NEW("NEW"),
    ACTIVE("ACTIVE"),
    REJECTED("REJECTED"),
    FINISHED("FINISHED"),
    BLOCKED("BLOCKED"),
    RESTICK("RESTICK"),
    PRETERM_LEAVE("PRETERM_LEAVE");


    private String id;

    EAdvDriverStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EAdvDriverStatus fromId(String id) {
        for (EAdvDriverStatus at : EAdvDriverStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}