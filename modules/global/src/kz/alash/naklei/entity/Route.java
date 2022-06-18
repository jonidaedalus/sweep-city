package kz.alash.naklei.entity;

import com.haulmont.addon.maps.gis.Geometry;
import com.haulmont.addon.maps.gis.converters.wkt.CubaLineStringWKTConverter;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "NAKLEI_ROUTE")
@Entity(name = "naklei_Route")
@NamePattern("%s|points")
public class Route extends StandardEntity {
    private static final long serialVersionUID = 4134094040234203097L;

    @Geometry
    @MetaProperty(datatype = "GeoPolyline", mandatory = true)
    @Column(name = "LINE", nullable = false, columnDefinition = "text", length = 10485760)
    @Convert(converter = CubaLineStringWKTConverter.class)
    @NotNull
    private LineString line;

    //TODO Сделать обязательным
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_", nullable = false)
    @NotNull
    private Date date;

    @Column(name = "TIME_")
    private Double time;

    @Column(name = "OTS")
    private Double ots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADVERTISEMENT_DRIVER_ID")
    private AdvertisementDriver advertisementDriver;

    //todo not null
    @Column(name = "POINTS", nullable = false, columnDefinition = "double precision default 0.0")
    @NotNull
    private Double points = 0.0;

    @Column(name = "DISTANCE", nullable = false, columnDefinition = "double precision default 0.0")
    @NotNull
    private Double distance = 0.0;

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getTime() {
        return time;
    }

    public Double getOts() {
        return ots;
    }

    public void setOts(Double ots) {
        this.ots = ots;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getPoints() {
        return points;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public AdvertisementDriver getAdvertisementDriver() {
        return advertisementDriver;
    }

    public void setAdvertisementDriver(AdvertisementDriver advertisementDriver) {
        this.advertisementDriver = advertisementDriver;
    }

    public LineString getLine() {
        return line;
    }

    public void setLine(LineString line) {
        this.line = line;
    }


}