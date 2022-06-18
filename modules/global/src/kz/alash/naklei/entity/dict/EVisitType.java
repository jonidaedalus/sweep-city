package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EVisitType implements EnumClass<String> {

    CARWASH("CARWASH", "event-blue", "BLUE"),
    STICK("STICK", "event-purple", "PURPLE");

    private final String id;
    private final String styleName;
    private final String icon;


    EVisitType(String value, String styleName, String icon) {
        this.id = value;
        this.styleName = styleName;
        this.icon = icon;
    }


    public String getId() {
        return id;
    }

    @Nullable
    public static EVisitType fromId(String id) {
        for (EVisitType at : EVisitType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getIcon() {
        return icon;
    }
}