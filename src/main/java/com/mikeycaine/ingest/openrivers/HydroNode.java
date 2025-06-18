package com.mikeycaine.ingest.openrivers;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="hydro_node")
//@Proxy(lazy = false)
@NoArgsConstructor
public class HydroNode {

    public HydroNode(String id, String nodeCategory, Point location) {
        this.id = id;
        this.nodeCategory = nodeCategory;
        this.location = location;
    }

    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String nodeCategory;

    @Column(name = "location", nullable = false, columnDefinition="Geometry")
    @Getter
    @Setter
    @JsonIgnore
    private Point location;
}
