package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "NAKLEI_D_COVERAGE")
@Entity(name = "naklei_DCoverage")
@NamePattern("%s|value")
public class DCoverage extends BaseUuidEntity {
    private static final long serialVersionUID = -835908588876088940L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STICKER_TYPE_ID")
    private DStickerType stickerType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ZONE_ID")
    private DZone zone;

    @NotNull
    @Column(name = "VALUE_", nullable = false)
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public DZone getZone() {
        return zone;
    }

    public void setZone(DZone zone) {
        this.zone = zone;
    }

    public DStickerType getStickerType() {
        return stickerType;
    }

    public void setStickerType(DStickerType stickerType) {
        this.stickerType = stickerType;
    }
}