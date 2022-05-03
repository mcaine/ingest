package com.mikeycaine.ingest;

import lombok.RequiredArgsConstructor;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AdminUnitController {

    private final AdministrativeUnitRepository repo;

    @GetMapping("/adminarea/{id}")
    public AdministrativeUnit adminarea (@PathVariable String id){
        return repo.getById(id);
    }

    @GetMapping("/boundary/{id}")
    public String boundary(@PathVariable String id) throws FactoryException, IOException {

        String crs = "EPSG:27700";
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder multipolygonTypeBuilder = new SimpleFeatureTypeBuilder();
        multipolygonTypeBuilder.setName("Administrative Area");
        multipolygonTypeBuilder.setCRS(osgbCrs);
        multipolygonTypeBuilder.add("id", String.class);
        multipolygonTypeBuilder.add("name", String.class);
        multipolygonTypeBuilder.add("boundary", MultiPolygon.class);

        final SimpleFeatureType featureType = multipolygonTypeBuilder.buildFeatureType();

        AdministrativeUnit administrativeUnit = repo.getById(id);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null,null);

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        featureBuilder.add(administrativeUnit.getId());
        featureBuilder.add(administrativeUnit.getName());
        featureBuilder.add(administrativeUnit.getBoundary());

        SimpleFeature feature = featureBuilder.buildFeature(null);
        featureCollection.add(feature);


        FeatureJSON fj = new FeatureJSON();
        fj.setEncodeFeatureCRS(true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fj.writeFeatureCollection(featureCollection, os);
        os.close();

        return os.toString();
    }
}
