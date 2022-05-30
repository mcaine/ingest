package com.mikeycaine.ingest.openrivers;

import lombok.RequiredArgsConstructor;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RiverController {

    private final WatercourseLinkRepository watercourseLinkRepository;

    @GetMapping("/river/{name}")
    public String river(@PathVariable String name) throws FactoryException, IOException {
        List<WatercourseLink> links = watercourseLinkRepository.findByName(name);
        return riverFeaturesJson(links);
    }

    private String riverFeaturesJson(List<WatercourseLink> links) throws IOException, FactoryException {
        String crs = "EPSG:27700";
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setName("Watercourse Link");
        featureTypeBuilder.setCRS(osgbCrs);
        featureTypeBuilder.add("id", String.class);
        featureTypeBuilder.add("name", String.class);
        featureTypeBuilder.add("form", String.class);
        featureTypeBuilder.add("centreLine", LineString.class);

        final SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null, featureType);

        for (WatercourseLink link: links) {
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.add(link.getId());
            featureBuilder.add(link.getName());
            featureBuilder.add(link.getForm());
            featureBuilder.add(link.getLineString());
            SimpleFeature feature = featureBuilder.buildFeature(link.getId());
            featureCollection.add(feature);
        }

        FeatureJSON fj = new FeatureJSON();
        fj.setEncodeFeatureCRS(true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fj.writeFeatureCollection(featureCollection, os);
        os.close();

        return os.toString();
    }

}
