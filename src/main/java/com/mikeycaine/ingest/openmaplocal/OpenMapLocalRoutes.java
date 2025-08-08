package com.mikeycaine.ingest.openmaplocal;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenMapLocalRoutes extends RouteBuilder {

    private final OpenMapLocalFeatureCollectionProcessor openMapLocalFeatureCollectionProcessor;
    private final RailwayStationProcessor railwayStationProcessor;
    private final RailwayTrackProcessor railwayTrackProcessor;
    private final RailwayTunnelProcessor railwayTunnelProcessor;

    final static String OPENMAPLOCAL_DATA = "E:\\Downloads\\opmplc_gml3_gb\\data";

    @Override
    public void configure() throws Exception {
        from("file:" + OPENMAPLOCAL_DATA + "?noop=true&recursive=true")
            .routeId("Open Map Local")
            .autoStartup(false)
            .log("Open Map Local ${file:path}")
            .unmarshal(jaxbDataFormat())
            .process(openMapLocalFeatureCollectionProcessor)
            .split(body())
            .streaming()
            .choice()
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayTrackType'"))
                    .process(railwayTrackProcessor)
                    .to("jpa:RailwayTrack")

                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayStationType'"))
                    .process(railwayStationProcessor)
                    .to("jpa:RailwayStation")

                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayTunnelType'"))
                    .process(railwayTunnelProcessor)
                    .to("jpa:RailwayTunnel")

                .otherwise().end();
                    //.to("log:unknown");
    }

    private JaxbDataFormat jaxbDataFormat() throws JAXBException {
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        jaxbDataFormat.setContext(JAXBContext.newInstance(
            uk.os.namespaces.open.oml._1.ObjectFactory.class,
            uk.os.namespaces.product._1.ObjectFactory.class
        ));
        return jaxbDataFormat;
    }
}
