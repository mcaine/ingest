package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.config.FileEndpointUris;
import com.mikeycaine.ingest.config.IngestInputProperties;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import net.opengis.gml._3.FeaturePropertyType;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.support.builder.Namespaces;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenMapLocalRoutes extends RouteBuilder {

    private final RailwayStationProcessor railwayStationProcessor;
    private final RailwayTrackProcessor railwayTrackProcessor;
    private final RailwayTunnelProcessor railwayTunnelProcessor;
    private final IngestInputProperties input;

    private static final Namespaces NAMESPACES =
        new Namespaces("os", "http://namespaces.os.uk/product/1.0");

    @Override
    public void configure() throws Exception {
        IngestInputProperties.OpenMapLocal openMapLocal = input.openMapLocal();
        from(FileEndpointUris.directoryOnly(openMapLocal.directory(),
                Map.of("noop", true, "recursive", true)))
            .routeId("Open Map Local")
            .autoStartup(false)
            .log("Open Map Local ${file:path}")
            .split().xtokenize("//os:featureMember", 'i', NAMESPACES)
            .streaming()
            .unmarshal(jaxbDataFormat())
            .process(exchange -> {
                Object body = exchange.getIn().getBody();
                if (body instanceof JAXBElement<?> element) {
                    body = element.getValue();
                }
                if (body instanceof FeaturePropertyType featureMember) {
                    body = featureMember.getAbstractFeature().getValue();
                }
                exchange.getIn().setBody(body);
            })
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
        jaxbDataFormat.setPartClass(FeaturePropertyType.class);
        return jaxbDataFormat;
    }
}
