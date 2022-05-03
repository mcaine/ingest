package com.mikeycaine.ingest;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="administrative_unit")
@Proxy(lazy = false)
public class AdministrativeUnit {
    @Id
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private String id;

    @Column(name = "boundary", nullable = false)
    @Getter
    @Setter
    private MultiPolygon boundary;

    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "level", nullable = false)
    @Getter
    @Setter
    private String level;


    @Column(name = "code", nullable = false)
    @Getter
    @Setter
    private String code;
}
