package kz.alash.naklei.web.screens.visit;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import org.springframework.lang.NonNullApi;

import javax.annotation.Nullable;


public enum ECalendarMode implements EnumClass<String> {

    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH");

    private String id;

    ECalendarMode(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ECalendarMode fromId(String id) {
        for (ECalendarMode at : ECalendarMode.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}