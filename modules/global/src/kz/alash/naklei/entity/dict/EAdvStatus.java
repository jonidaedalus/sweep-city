package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EAdvStatus implements EnumClass<String> {

    IN_PROCESS("IN_PROCESS"),//Драфт(В работе)
    ACTIVE("ACTIVE"),//Активная
    PENDING("PENDING"),//В ожидании
    COMPLETED("COMPLETED"),//В ожидании
    CANCELED("CANCELED");//Архивный

    private String id;

    EAdvStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EAdvStatus fromId(String id) {
        for (EAdvStatus at : EAdvStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}