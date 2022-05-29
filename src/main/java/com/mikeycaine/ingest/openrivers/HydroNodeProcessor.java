package com.mikeycaine.ingest.openrivers;

import lombok.extern.slf4j.Slf4j;

import inspire.x.specification.gmlas.hydronetwork._3.HydroNodeType.HydroNodeCategory;
import net.opengis.gml._3.DirectPositionType;
import net.opengis.gml._3.PointPropertyType;
import net.opengis.gml._3.PointType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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

        String nodeCategory = Optional.ofNullable(hydroNodeType.getHydroNodeCategory())
            .flatMap(hnc -> Optional.ofNullable(hnc.getValue()))
            .orElse("???");

        Optional<List<Double>> posnValues = Optional.ofNullable(hydroNodeType.getGeometry())
            .flatMap(geom -> Optional.ofNullable(geom.getPoint()))
            .flatMap(pt -> Optional.ofNullable(pt.getPos()))
            .flatMap(dp -> Optional.ofNullable(dp.getValue()));

        System.out.print(nodeCategory + " ");

        if (posnValues.isPresent()) {
            List<Double> xy = posnValues.get();
            System.out.println(xy.get(0) + " " + xy.get(1));
        } else {
            System.out.println("NONE");
        }
    }
}
