package kz.alash.naklei.entity.dict;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "NAKLEI_D_CITY")
@Entity(name = "naklei_DCity")
@NamePattern("%s|name")
public class DCity extends StandardEntity {
    private static final long serialVersionUID = -8143691271133331804L;

    public static final String ALMATY_CITY_CODE = "ALA";

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "city")
    private List<DZone> zones;

    @Column(name = "CODE", nullable = false)
    @NotNull
    private String code;

    @NotNull
    @Column(name = "XPOINT", nullable = false)
    private Double xpoint;

    @NotNull
    @Column(name = "YPOINT", nullable = false)
    private Double ypoint;

    @Column(name = "ZOOM_LEVEL", nullable = false)
    @NotNull
    private Double zoomLevel;

    public void setZones(List<DZone> zones) { this.zones = zones; }

    public List<DZone> getZones() { return zones; }

    public void setZoomLevel(Double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public Double getZoomLevel() {
        return zoomLevel;
    }

    public Double getYpoint() {
        return ypoint;
    }

    public void setYpoint(Double ypoint) {
        this.ypoint = ypoint;
    }

    public Double getXpoint() {
        return xpoint;
    }

    public void setXpoint(Double xpoint) {
        this.xpoint = xpoint;
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