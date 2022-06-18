package kz.alash.naklei.entity;

import com.haulmont.addon.maps.gis.Geometry;
import com.haulmont.addon.maps.gis.converters.wkt.CubaPointWKTConverter;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_ADV_LOCATION")
@Entity(name = "naklei_AdvLocation")
@NamePattern("%s|name")
public class AdvLocation extends StandardEntity {
    private static final long serialVersionUID = 2466304597175293243L;

    @NotNull
    @Geometry
    @MetaProperty(datatype = "GeoPoint", mandatory = true)
    @Column(name = "LACATION", nullable = false)
    @Convert(converter = CubaPointWKTConverter.class)
    private Point location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADVERTISER_ID")
    private Advertiser advertiser;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}