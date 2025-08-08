package com.mikeycaine.ingest.openrivers;

import com.mikeycaine.ingest.Util;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import uk.os.namespaces.open.rivers._1.HydroNodeType;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HydroNodeProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        HydroNodeType hydroNodeType = exchange.getIn().getBody(HydroNodeType.class);
        if (hydroNodeType == null) {
            throw new RuntimeException("null hydro node body");
        }

        String id = hydroNodeType.getId();

        String nodeCategory = Optional.ofNullable(hydroNodeType.getHydroNodeCategory())
            .flatMap(hnc -> Optional.ofNullable(hnc.getValue()))
            .orElse(null);

        Optional<List<Double>> posnValues = Optional.ofNullable(hydroNodeType.getGeometry())
            .flatMap(geom -> Optional.ofNullable(geom.getPoint()))
            .flatMap(pt -> Optional.ofNullable(pt.getPos()))
            .flatMap(dp -> Optional.ofNullable(dp.getValue()));

        Point location = null;
        if (posnValues.isPresent()) {
            List<Double> xy = posnValues.get();
            location = Util.pointFrom(xy.get(0), xy.get(1));
        }

        HydroNode hydroNode = new HydroNode(id, nodeCategory, location);
        exchange.getIn().setBody(hydroNode, HydroNode.class);
    }
}