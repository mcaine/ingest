package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.Util;

import net.opengis.gml._3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import uk.os.namespaces.open.oml._1.RailwayTunnelType;

import javax.xml.bind.JAXBElement;
import java.util.Optional;

@Service
public class RailwayTunnelProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        RailwayTunnelType rtt = in.getBody(RailwayTunnelType.class);

        String id = rtt.getId();
        LineString lineString = Optional.ofNullable(rtt.getGeometry())
            .flatMap(geom -> Optional.ofNullable(geom.getAbstractCurve()))
            .filter(el -> el.getDeclaredType().equals(LineStringType.class))
            .map(e -> e.getValue())
            .flatMap(ls -> Optional.ofNullable(Util.convertLineString((LineStringType)ls)))
            .orElse(null);

        RailwayTunnel railwayTunnel =  new RailwayTunnel(id, lineString);
        in.setBody(railwayTunnel, RailwayTunnel.class);
    }
}
