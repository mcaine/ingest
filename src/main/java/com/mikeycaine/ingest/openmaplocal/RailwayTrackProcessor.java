package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.Util;
import net.opengis.gml._3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.Optional;

import uk.os.namespaces.open.oml._1.RailwayTrackType;

import javax.xml.bind.JAXBElement;

@Service
public class RailwayTrackProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        RailwayTrackType rtt = in.getBody(RailwayTrackType.class);

        String id = rtt.getFeatureCode().toString();
        LineString lineString = Optional.ofNullable(rtt.getGeometry())
            .flatMap(geom -> Optional.ofNullable(geom.getAbstractCurve()))
            .filter(el -> el.getDeclaredType().equals(LineStringType.class))
            .map(e -> ((JAXBElement<LineStringType>)e).getValue())
            .flatMap(ls -> Optional.ofNullable(Util.convertLineString(ls)))
            .orElse(null);

        RailwayTrack railwayTrack =  new RailwayTrack(id, lineString);
        in.setBody(railwayTrack, RailwayTrack.class);
    }
}
