package com.mikeycaine.ingest;

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
    private String id;

    @Column(name = "boundary", nullable = false)
    private MultiPolygon boundary;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "level", nullable = false)
    private String level;


    @Column(name = "code", nullable = false)
    private String code;


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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public MultiPolygon getBoundary() {
        return boundary;
    }

    public void setBoundary(MultiPolygon boundary) {
        this.boundary = boundary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
