package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EPayOutStatus implements EnumClass<String> {

    PAID("PAID"),
    IN_WORK("IN WORK");

    private String id;

    EPayOutStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EPayOutStatus fromId(String id) {
        for (EPayOutStatus at : EPayOutStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}