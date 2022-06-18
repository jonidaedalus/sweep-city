package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

//todo на DContractLength
public enum EContractTime implements EnumClass<Integer> {

    ONE_MONTH(1),
    TWO_MONTHS(2),
    THREE_MONTHS(3),
    HALF_YEAR(6),
    YEAR(12),
    OTHER(0);

    private final Integer id;

    EContractTime(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EContractTime fromId(Integer id) {
        for (EContractTime at : EContractTime.values()) {
            if (at.getId() != null && at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}