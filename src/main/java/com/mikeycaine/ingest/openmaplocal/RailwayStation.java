package com.mikeycaine.ingest.openmaplocal;

//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.mikeycaine.ingest.GeometrySerializerForLocationTech;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="railway_station")
//@Proxy(lazy = false)
@NoArgsConstructor
public class RailwayStation {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Column(name = "location", nullable = false, columnDefinition="Geometry")
    //@JsonSerialize(using = GeometrySerializerForLocationTech.class)
    private Point location;

    public RailwayStation(String id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
