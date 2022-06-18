package kz.alash.naklei.entity.dict.car;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_D_MODEL")
@Entity(name = "naklei_DModel")
@NamePattern("%s|name")
public class DModel extends BaseUuidEntity {
    private static final long serialVersionUID = -5708804533170610638L;

    @Column(name = "NAME")
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MARK_ID")
    private DMark mark;

    @Column(name = "CODE", nullable = false, unique = true)
    @NotNull
    private String code;

    public DMark getMark() {
        return mark;
    }

    public void setMark(DMark mark) {
        this.mark = mark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}