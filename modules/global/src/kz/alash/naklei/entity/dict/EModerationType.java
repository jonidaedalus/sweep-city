package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EModerationType implements EnumClass<String> {

    PRECHECK("Премодерация"),
    CARWASH("Мойка"),
    START("Начало"),
    FINISH("Конец"),
    VERIFY("Верифай"),
    PAYOUT("Выплата");

    private String id;

    EModerationType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EModerationType fromId(String id) {
        for (EModerationType at : EModerationType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}