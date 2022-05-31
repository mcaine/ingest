package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.openrivers.WatercourseLink;
import com.mikeycaine.ingest.openrivers.WatercourseLinkRepository;
import lombok.RequiredArgsConstructor;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RailwayController {

    private final String crs = "EPSG:27700";

    private final RailwayStationRepository railwayStationRepository;
    private final RailwayTrackRepository railwayTrackRepository;
    private final RailwayTunnelRepository railwayTunnelRepository;

    @GetMapping("/railways")
    public String railways() throws FactoryException, IOException {
        List<RailwayTrack> tracks = railwayTrackRepository.findAll();
        List<RailwayStation> stations = railwayStationRepository.findAll();
        List<RailwayTunnel> tunnels = railwayTunnelRepository.findAll();

        return railwayFeaturesJson(tracks, stations, tunnels);
    }

    private String railwayFeaturesJson(
        List<RailwayTrack> tracks,
        List<RailwayStation> stations,
        List<RailwayTunnel> tunnels) throws IOException, FactoryException {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null, null);

        addTracksToFeatureCollection(tracks, featureCollection);
        addStationsToFeatureCollection(stations, featureCollection);
        addTunnelsToFeatureCollection(tunnels, featureCollection);


        FeatureJSON fj = new FeatureJSON();
        fj.setEncodeFeatureCRS(true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fj.writeFeatureCollection(featureCollection, os);
        os.close();

        return os.toString();
    }

    private void addTunnelsToFeatureCollection(List<RailwayTunnel> tunnels, DefaultFeatureCollection featureCollection) throws FactoryException {
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway Tunnel");
        ftb.setCRS(osgbCrs);
        ftb.add("id", String.class);
        ftb.add("what", String.class);
        ftb.add("lineString", LineString.class);

        final SimpleFeatureType ft = ftb.buildFeatureType();

        for (RailwayTunnel tunnel: tunnels) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
            fb.add(tunnel.getId());
            fb.add("tunnel");
            fb.add(tunnel.getLineString());
            SimpleFeature feature = fb.buildFeature(tunnel.getId());
            featureCollection.add(feature);
        }
    }

    void addStationsToFeatureCollection(Collection<RailwayStation> stations, DefaultFeatureCollection featureCollection) throws FactoryException {
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway Station");
        ftb.setCRS(osgbCrs);
        ftb.add("id", String.class);
        ftb.add("name", String.class);
        ftb.add("what", String.class);
        ftb.add("location", Point.class);

        final SimpleFeatureType ft = ftb.buildFeatureType();

        for (RailwayStation station: stations) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
            fb.add(station.getId());
            fb.add(station.getName());
            fb.add("station");
            fb.add(station.getLocation());
            SimpleFeature feature = fb.buildFeature(station.getId());
            featureCollection.add(feature);
        }
    }

    void addTracksToFeatureCollection(Collection<RailwayTrack> tracks, DefaultFeatureCollection featureCollection) throws FactoryException {
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway Track");
        ftb.setCRS(osgbCrs);
        ftb.add("id", String.class);
        ftb.add("what", String.class);
        ftb.add("lineString", LineString.class);

        final SimpleFeatureType ft = ftb.buildFeatureType();

        for (RailwayTrack track: tracks) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
            fb.add(track.getId());
            fb.add("track");
            fb.add(track.getLineString());
            SimpleFeature feature = fb.buildFeature(track.getId());
            featureCollection.add(feature);
        }
    }

}
