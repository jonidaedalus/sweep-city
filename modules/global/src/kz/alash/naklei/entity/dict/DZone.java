package kz.alash.naklei.entity.dict;

import com.haulmont.addon.maps.gis.Geometry;
import com.haulmont.addon.maps.gis.converters.wkt.CubaPolygonWKTConverter;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_D_ZONE")
@Entity(name = "naklei_DZone")
@NamePattern("%s|name")
public class DZone extends BaseUuidEntity {
    private static final long serialVersionUID = -3917000063408668502L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_TYPE_ID")
    private DZoneType zoneType;

    @Geometry
    @MetaProperty(datatype = "GeoPolygon", mandatory = true)
    @Column(name = "POLYGON", nullable = false)
    @Convert(converter = CubaPolygonWKTConverter.class)
    @NotNull
    private Polygon polygon;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @NotNull
    @Column(name = "CUSTOM", nullable = false)
    private Boolean custom = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CITY_ID")
    @NotNull
    private DCity city;

    @NotNull
    @Column(name = "IS_CITY", nullable = false)
    private Boolean main = false;

    public DZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(DZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public DCity getCity() {
        return city;
    }

    public void setCity(DCity city) {
        this.city = city;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon poligon) {
        this.polygon = poligon;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
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