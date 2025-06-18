package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.openrivers.HydroNodeProcessor;
import com.mikeycaine.ingest.openrivers.WatercourseLinkProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.support.builder.Namespaces;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

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
            .routeId("openmaplocal")
            .autoStartup(true)
            .log("OpenMapLocal ${file:path}")
            .unmarshal(jaxbDataFormat())
            .process(openMapLocalFeatureCollectionProcessor)
            .split(body())
            .streaming()
            .choice()
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.BuildingType'"))
                    .to("direct:buildings")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.SurfaceWaterLineType'"))
                    .to("direct:surfacewaterline")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.TidalBoundaryType'"))
                    .to("direct:tidalboundary")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.WoodlandType'"))
                    .to("direct:woodland")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RoadType'"))
                    .to("direct:road")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.SurfaceWaterAreaType'"))
                    .to("direct:surfacewaterarea")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.TidalWaterType'"))
                    .to("direct:tidalwater")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.ForeshoreType'"))
                    .to("direct:foreshore")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.NamedPlaceType'"))
                    .to("direct:namedplace")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.CarChargingPointType'"))
                    .to("direct:carchargingpoint")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.FunctionalSiteType'"))
                    .to("direct:functionalsite")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.ImportantBuildingType'"))
                    .to("direct:importantbuilding")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayTrackType'"))
                    .to("direct:railwaytrack")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RoundaboutType'"))
                    .to("direct:roundabout")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.ElectricityTransmissionLineType'"))
                    .to("direct:electricitytransmissionline")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayStationType'"))
                    .to("direct:railwaystation")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RailwayTunnelType'"))
                    .to("direct:railwaytunnel")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.RoadTunnelType'"))
                    .to("direct:roadtunnel")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.GlasshouseType'"))
                    .to("direct:glasshouse")
                .when(simple("${body} is 'uk.os.namespaces.open.oml._1.MotorwayJunctionType'"))
                    .to("direct:motorwayjunction")
                .otherwise()
                    .to("log:unknown");

        from("direct:buildings").stop();
        from("direct:surfacewaterline").stop();
        from("direct:tidalboundary").stop();
        from("direct:woodland").stop();
        from("direct:road").stop();
        from("direct:surfacewaterarea").stop();
        from("direct:tidalwater").stop();
        from("direct:foreshore").stop();
        from("direct:namedplace").stop();
        from("direct:carchargingpoint").stop();
        from("direct:functionalsite").stop();
        from("direct:importantbuilding").stop();
        from("direct:railwaytrack").process(railwayTrackProcessor).to("jpa:RailwayTrack");
        from("direct:roundabout").stop();
        from("direct:electricitytransmissionline").stop();
        from("direct:railwaystation").process(railwayStationProcessor).to("jpa:RailwayStation");
        from("direct:railwaytunnel").process(railwayTunnelProcessor).to("jpa:RailwayTunnel");
        from("direct:roadtunnel").stop();
        from("direct:glasshouse").stop();
        from("direct:motorwayjunction").stop();
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
