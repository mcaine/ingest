package com.mikeycaine.ingest.openrivers;

import com.mikeycaine.ingest.Util;
import lombok.extern.slf4j.Slf4j;
import net.opengis.gml._3.AbstractCurveType;
import net.opengis.gml._3.CurvePropertyType;
import net.opengis.gml._3.LineStringType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import uk.os.namespaces.open.rivers._1.WatercourseLinkType;

import java.util.Optional;

@Service
@Slf4j
public class WatercourseLinkProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        //System.out.println(exchange);
        WatercourseLinkType wlt = exchange.getIn().getBody(WatercourseLinkType.class);
        if (wlt == null) {
            throw new RuntimeException("NULL wlt");
        }

        Optional<Double> len = Optional.ofNullable(wlt.getLength()).map(l -> l.getValue());

        String form = Optional.ofNullable(wlt.getForm())
            .flatMap(f -> Optional.ofNullable(f.getValue())).orElse(null);

        String name = Optional.ofNullable(wlt.getWatercourseName()).flatMap(nm -> Optional.ofNullable(nm.getValue())).orElse(null);

        String id = wlt.getId();


        Optional<CurvePropertyType> clg = Optional.ofNullable(wlt.getCentrelineGeometry());

        Optional<LineStringType> lineStringType = clg.flatMap(c -> Optional.ofNullable(c.getAbstractCurve()))
            .filter(el -> el.getDeclaredType().equals(LineStringType.class))
            .map(e -> (LineStringType) e.getValue());

        LineString lineString = lineStringType.map(l -> Util.convertLineString(l)).orElse(null);

        String title = clg.flatMap(c -> Optional.ofNullable(c.getTitle())).orElse(null);

        //System.out.println(form + " " + name + " " + lineString);

        WatercourseLink watercourseLink = new WatercourseLink(id, name, form, lineString);

        exchange.getIn().setBody(watercourseLink);


    }
}
