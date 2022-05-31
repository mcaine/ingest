package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.Util;
import lombok.RequiredArgsConstructor;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class RailwayStationController {

    private final String crsName = "EPSG:27700";

    private final RailwayStationRepository railwayStationRepository;

    @GetMapping("/railwayStations")
    public String railwayStations() throws FactoryException, IOException {
        return Util.featureCollectionAsString(stationsFeatureCollection(railwayStationRepository.findAll()));
    }

    DefaultFeatureCollection stationsFeatureCollection(Collection<RailwayStation> stations) throws FactoryException {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("Railway Stations", railwayStationFeatureType());
        for (RailwayStation station: stations) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(railwayStationFeatureType());
            fb.add(station.getId());
            fb.add(station.getName());
            fb.add(station.getLocation());
            featureCollection.add(fb.buildFeature(station.getId()));
        }
        return featureCollection;
    }

    SimpleFeatureType railwayStationFeatureType() throws FactoryException {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway Station");
        ftb.setCRS(Util.crs(crsName));
        ftb.add("id", String.class);
        ftb.add("name", String.class);
        ftb.add("location", Geometry.class);
        return ftb.buildFeatureType();
    }
//
//    CoordinateReferenceSystem crs() throws FactoryException {
//        return CRS.decode(crsName);
//    }
//
//    String featureCollectionAsString(DefaultFeatureCollection featureCollection) throws IOException {
//        FeatureJSON fj = new FeatureJSON();
//        fj.setEncodeFeatureCRS(true);
//
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            fj.writeFeatureCollection(featureCollection, os);
//            return os.toString();
//        }
//    }
}
