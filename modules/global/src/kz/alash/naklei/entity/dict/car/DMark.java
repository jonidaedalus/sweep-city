package kz.alash.naklei.entity.dict.car;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "NAKLEI_D_MARK")
@Entity(name = "naklei_DMark")
@NamePattern("%s|name")
public class DMark extends BaseUuidEntity {
    private static final long serialVersionUID = 917573616575504801L;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "mark")
    private List<DModel> models;

    @Column(name = "CODE", nullable = false, unique = true)
    @NotNull
    private String code;

    public List<DModel> getModels() {
        return models;
    }

    public void setModels(List<DModel> models) {
        this.models = models;
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