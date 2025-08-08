package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.Util;
import net.opengis.gml._3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import uk.os.namespaces.open.oml._1.RailwayStationType;

import java.util.List;
import java.util.Optional;

@Service
public class RailwayStationProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        RailwayStationType rst = in.getBody(RailwayStationType.class);

        String id = rst.getId();
        String name = rst.getDistinctiveName();

        Optional<DirectPositionType> dpOption = Optional.ofNullable(rst.getGeometry())
            .flatMap(geom -> Optional.ofNullable(geom.getPoint()))
            .flatMap(pt -> Optional.ofNullable(((PointType)pt).getPos()));

        Point location = Util.pointFromDirectPositionOption(dpOption);

        RailwayStation railwayStation = new RailwayStation(id, name, location);
        in.setBody(railwayStation, RailwayStation.class);

    }
}
