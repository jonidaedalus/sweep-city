package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "NAKLEI_D_TRAFFIC_RATE")
@Entity(name = "naklei_DTrafficRate")
@NamePattern("%s|name")
public class DTrafficRate extends BaseUuidEntity {
    private static final long serialVersionUID = 1930761038724841805L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "VALUE_")
    private Integer value;

    @Column(name = "AVERAGE_SPEED")
    private Integer averageSpeed;

    public Integer getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Integer averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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