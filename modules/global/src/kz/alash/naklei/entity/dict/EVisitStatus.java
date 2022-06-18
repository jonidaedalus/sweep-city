package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EVisitStatus implements EnumClass<String> {

    DONE("DONE"), //Выполнена
    PENDING("PENDING"), //В ожидании
    CANCELED("CANCELED"); //Отменена

    private String id;

    EVisitStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EVisitStatus fromId(String id) {
        for (EVisitStatus at : EVisitStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}