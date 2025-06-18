package com.mikeycaine.ingest.openrivers;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Component
@RequiredArgsConstructor
public class OpenRiversRoutes extends RouteBuilder {

    private final WatercourseLinkProcessor watercourseLinkProcessor;
    private final HydroNodeProcessor hydroNodeProcessor;
    private final OpenRiversFeatureCollectionProcessor openRiversFeatureCollectionProcessor;

    final static String OPENRIVERS_DATA = "E:\\Downloads\\oprvrs_gml3_gb\\data?fileName=OSOpenRivers.gml";

    @Override
    public void configure() throws Exception {
        from("file:" + OPENRIVERS_DATA + "&noop=true")
            .routeId("openrivers")
            .autoStartup(true)
            .log("OpenRivers ${file:path}")
            .unmarshal(jaxbDataFormat())
            .process(openRiversFeatureCollectionProcessor)
            .split(body())
            .streaming()
            .choice()
                .when(simple("${body} is 'uk.os.namespaces.open.rivers._1.HydroNodeType'"))
                    .to("direct:hydronodes")
                .when(simple("${body} is 'uk.os.namespaces.open.rivers._1.WatercourseLinkType'"))
                    .to("direct:watercourselink")
                .otherwise()
                    .to("log:unknown");

        from("direct:hydronodes").process(hydroNodeProcessor).to("jpa:HydroNode");
        from("direct:watercourselink").process(watercourseLinkProcessor).to("jpa:WatercourseLink");
    }

    private JaxbDataFormat jaxbDataFormat() throws JAXBException {
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        JAXBContext jaxbContext = JAXBContext.newInstance(
            uk.os.namespaces.open.rivers._1.ObjectFactory.class,
            inspire.x.specification.gmlas.hydronetwork._3.ObjectFactory.class,
            uk.os.namespaces.product._1.ObjectFactory.class,
            net.opengis.gml._3.ObjectFactory.class
        );
        jaxbDataFormat.setContext(jaxbContext);
        return jaxbDataFormat;
    }
}
