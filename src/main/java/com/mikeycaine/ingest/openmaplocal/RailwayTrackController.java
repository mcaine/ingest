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
public class RailwayTrackController {

    private final String crsName = "EPSG:27700";

    private final RailwayTrackRepository railwayTrackRepository;

    @GetMapping("/railwayTracks")
    public String railwayTracks() throws FactoryException, IOException {
        return Util.featureCollectionAsString(tracksFeatureCollection(railwayTrackRepository.findAll()));
    }

    DefaultFeatureCollection tracksFeatureCollection(Collection<RailwayTrack> tracks) throws FactoryException {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("Railway Tracks", railwayTrackFeatureType());
        for (RailwayTrack track: tracks) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(railwayTrackFeatureType());
            fb.add(track.getId());
            fb.add(track.getLineString());
            featureCollection.add(fb.buildFeature(track.getId()));
        }
        return featureCollection;
    }

    SimpleFeatureType railwayTrackFeatureType() throws FactoryException {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway track");
        ftb.setCRS(Util.crs(crsName));
        ftb.add("id", String.class);
        ftb.add("location", Geometry.class);
        return ftb.buildFeatureType();
    }
}
