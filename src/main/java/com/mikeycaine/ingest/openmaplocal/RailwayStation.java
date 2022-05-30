package com.mikeycaine.ingest.openmaplocal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="railway_station")
@Proxy(lazy = false)
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
    private Point location;

    public RailwayStation(String id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
