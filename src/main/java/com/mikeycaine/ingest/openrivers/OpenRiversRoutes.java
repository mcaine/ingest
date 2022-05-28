package com.mikeycaine.ingest.openrivers;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;

@Component
@RequiredArgsConstructor
public class OpenRiversRoutes extends RouteBuilder {

    private final WatercourseLinkProcessor watercourseLinkProcessor;
    private final HydroNodeProcessor hydroNodeProcessor;

    final static String OPENRIVERS_DATA = "E:\\Downloads\\oprvrs_gml3_gb\\data?fileName=OSOpenRivers.gml";

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();

        JAXBContext jaxbContext = JAXBContext.newInstance(
            uk.os.namespaces.open.rivers._1.ObjectFactory.class,
            inspire.x.specification.gmlas.hydronetwork._3.ObjectFactory.class,
            uk.os.namespaces.open.rivers._1.WatercourseLinkType.class,
            uk.os.namespaces.product._1.ObjectFactory.class,
            net.opengis.gml._3.ObjectFactory.class
        );

        jaxbDataFormat.setContext(jaxbContext);

        Namespaces namespaces = new Namespaces()
            .add("os", "http://namespaces.os.uk/product/1.0")
            .add("river", "http://namespaces.os.uk/Open/Rivers/1.0");

        from("file:" + OPENRIVERS_DATA + "&noop=true")
            .multicast().to("seda:watercourselinks", "seda:hydronodes");

        //from("file:" + OPENRIVERS_DATA + "&noop=true")
        from("seda:watercourselinks")
            .routeId("watercourselinks")
            .autoStartup(true)
            .split(xpath("//river:WatercourseLink", namespaces))
            .streaming()
            .unmarshal(jaxbDataFormat)
            .to("log:watercourselinks");

        //from("file:" + OPENRIVERS_DATA + "&noop=true")
        from("seda:hydronodes")
            .routeId("hydronodes")
            .autoStartup(true)
            .split(xpath("//river:HydroNode", namespaces))
            .streaming()
            .unmarshal(jaxbDataFormat)
            .to("log:hydronodes");
    }
}
