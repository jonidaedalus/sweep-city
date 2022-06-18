package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EActivity implements EnumClass<String> {

    ON_THE_WAY("В пути"),
    STOPPED("На стоянке");

    private String id;

    EActivity(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EActivity fromId(String id) {
        for (EActivity at : EActivity.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}