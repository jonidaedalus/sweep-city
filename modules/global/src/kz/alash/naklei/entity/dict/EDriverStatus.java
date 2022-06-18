package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;
import java.math.BigDecimal;


public enum EDriverStatus implements EnumClass<String> {

    GOLD("Золотой", BigDecimal.valueOf(1.2)),
    SILVER("Серебряный", BigDecimal.valueOf(1.1)),
    BRONZE("Бронзовый", BigDecimal.ONE);

    private String id;
    private BigDecimal pointCoef;

    EDriverStatus(String value, BigDecimal pointCoef) {
        this.id = value;
        this.pointCoef = pointCoef;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getPointCoef(){
        return pointCoef;
    }

    @Nullable
    public static EDriverStatus fromId(String id) {
        for (EDriverStatus at : EDriverStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}