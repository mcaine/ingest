package com.mikeycaine.ingest.openrivers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.LineString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="watercourse_link")
@Proxy(lazy = false)
@NoArgsConstructor
public class WatercourseLink {
    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String form;
    @Getter
    @Setter
    @Column(name = "centreLine", nullable = false, columnDefinition="Geometry")
    private LineString lineString;

    public WatercourseLink(String id, String name, String form, LineString lineString) {
        this.id = id;
        this.name = name;
        this.form = form;
        this.lineString = lineString;
    }
}
