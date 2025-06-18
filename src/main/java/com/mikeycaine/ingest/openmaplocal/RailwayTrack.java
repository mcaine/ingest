package com.mikeycaine.ingest.openmaplocal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.LineString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="railway_track")
//@Proxy(lazy = false)
@NoArgsConstructor
public class RailwayTrack {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @Column(name = "geometry", nullable = false, columnDefinition="Geometry")
    private LineString lineString;

    public RailwayTrack(String id, LineString lineString) {
        this.id = id;
        this.lineString = lineString;
    }
}
