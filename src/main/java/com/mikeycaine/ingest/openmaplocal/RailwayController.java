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

        SimpleFeatureTypeBuilder railwayTunnelfeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        railwayTunnelfeatureTypeBuilder.setName("Railway Tunnel");
        railwayTunnelfeatureTypeBuilder.setCRS(osgbCrs);
        railwayTunnelfeatureTypeBuilder.add("id", String.class);
        railwayTunnelfeatureTypeBuilder.add("what", String.class);
        railwayTunnelfeatureTypeBuilder.add("lineString", LineString.class);

        final SimpleFeatureType railwayTunnelFeatureType = railwayTunnelfeatureTypeBuilder.buildFeatureType();

        for (RailwayTunnel tunnel: tunnels) {
            SimpleFeatureBuilder railwayTrackfeatureBuilder = new SimpleFeatureBuilder(railwayTunnelFeatureType);
            railwayTrackfeatureBuilder.add(tunnel.getId());
            railwayTrackfeatureBuilder.add("track");
            railwayTrackfeatureBuilder.add(tunnel.getLineString());
            SimpleFeature feature = railwayTrackfeatureBuilder.buildFeature(tunnel.getId());
            featureCollection.add(feature);
        }
    }

    void addStationsToFeatureCollection(Collection<RailwayStation> stations, DefaultFeatureCollection featureCollection) throws FactoryException {
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder railwayTrackfeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        railwayTrackfeatureTypeBuilder.setName("Railway Station");
        railwayTrackfeatureTypeBuilder.setCRS(osgbCrs);
        railwayTrackfeatureTypeBuilder.add("id", String.class);
        railwayTrackfeatureTypeBuilder.add("name", String.class);
        railwayTrackfeatureTypeBuilder.add("what", String.class);
        railwayTrackfeatureTypeBuilder.add("location", Point.class);

        final SimpleFeatureType railwayTrackFeatureType = railwayTrackfeatureTypeBuilder.buildFeatureType();

        for (RailwayStation station: stations) {
            SimpleFeatureBuilder railwayTrackfeatureBuilder = new SimpleFeatureBuilder(railwayTrackFeatureType);
            railwayTrackfeatureBuilder.add(station.getId());
            railwayTrackfeatureBuilder.add(station.getName());
            railwayTrackfeatureBuilder.add("station");
            railwayTrackfeatureBuilder.add(station.getLocation());
            SimpleFeature feature = railwayTrackfeatureBuilder.buildFeature(station.getId());
            featureCollection.add(feature);
        }
    }

    void addTracksToFeatureCollection(Collection<RailwayTrack> tracks, DefaultFeatureCollection featureCollection) throws FactoryException {
        CoordinateReferenceSystem osgbCrs = CRS.decode(crs);

        SimpleFeatureTypeBuilder railwayTrackfeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        railwayTrackfeatureTypeBuilder.setName("Railway Track");
        railwayTrackfeatureTypeBuilder.setCRS(osgbCrs);
        railwayTrackfeatureTypeBuilder.add("id", String.class);
        railwayTrackfeatureTypeBuilder.add("what", String.class);
        railwayTrackfeatureTypeBuilder.add("lineString", LineString.class);

        final SimpleFeatureType railwayTrackFeatureType = railwayTrackfeatureTypeBuilder.buildFeatureType();

        for (RailwayTrack track: tracks) {
            SimpleFeatureBuilder railwayTrackfeatureBuilder = new SimpleFeatureBuilder(railwayTrackFeatureType);
            railwayTrackfeatureBuilder.add(track.getId());
            railwayTrackfeatureBuilder.add("track");
            railwayTrackfeatureBuilder.add(track.getLineString());
            SimpleFeature feature = railwayTrackfeatureBuilder.buildFeature(track.getId());
            featureCollection.add(feature);
        }
    }

}
