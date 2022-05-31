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
public class RailwayTunnelController {

    private final String crsName = "EPSG:27700";

    private final RailwayTunnelRepository railwayTunnelRepository;

    @GetMapping("/railwayTunnels")
    public String railwayTunnels() throws FactoryException, IOException {
        return Util.featureCollectionAsString(tunnelsFeatureCollection(railwayTunnelRepository.findAll()));
    }

    DefaultFeatureCollection tunnelsFeatureCollection(Collection<RailwayTunnel> tunnels) throws FactoryException {
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("Railway Tunnels", railwayTunnelFeatureType());
        for (RailwayTunnel tunnel: tunnels) {
            SimpleFeatureBuilder fb = new SimpleFeatureBuilder(railwayTunnelFeatureType());
            fb.add(tunnel.getId());
            fb.add(tunnel.getLineString());
            featureCollection.add(fb.buildFeature(tunnel.getId()));
        }
        return featureCollection;
    }

    SimpleFeatureType railwayTunnelFeatureType() throws FactoryException {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Railway Tunnel");
        ftb.setCRS(Util.crs(crsName));
        ftb.add("id", String.class);
        ftb.add("location", Geometry.class);
        return ftb.buildFeatureType();
    }


}
