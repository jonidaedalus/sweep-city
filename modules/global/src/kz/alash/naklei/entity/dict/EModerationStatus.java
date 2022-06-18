package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EModerationStatus implements EnumClass<String> {

    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    IN_CHECK("IN_CHECK"),
    IN_WORK("IN_WORK");

    private String id;

    EModerationStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EModerationStatus fromId(String id) {
        for (EModerationStatus at : EModerationStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}