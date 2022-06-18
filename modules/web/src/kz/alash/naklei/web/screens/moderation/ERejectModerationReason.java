package kz.alash.naklei.web.screens.moderation;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import kz.alash.naklei.entity.dict.EVisitStatus;

import javax.annotation.Nullable;

public enum ERejectModerationReason implements EnumClass<String> {
    DIRTY("DIRTY"),
    VISIBLE_DEFECT("VISIBLE_DEFECT"),
    OTHER("OTHER");

    private String id;

    ERejectModerationReason(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ERejectModerationReason fromId(String id) {
        for (ERejectModerationReason at : ERejectModerationReason.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
