package com.mikeycaine.ingest.openmaplocal;

import com.mikeycaine.ingest.openrivers.HydroNodeProcessor;
import com.mikeycaine.ingest.openrivers.WatercourseLinkProcessor;
import com.mikeycaine.ingest.terrain50.TerrainFilesAggregationStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.support.builder.Namespaces;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;

@Component
@RequiredArgsConstructor
public class OpenMapLocalRoutes extends RouteBuilder {

//    private final WatercourseLinkProcessor watercourseLinkProcessor;
//    private final HydroNodeProcessor hydroNodeProcessor;

    final static String OPENMAPLOCAL_DATA = "E:\\Downloads\\opmplc_gml3_gb\\data";

    @Override
    public void configure() throws Exception {
        from("file:" + OPENMAPLOCAL_DATA + "?noop=true&recursive=true")
            .routeId("openmaplocal unzip")
            .autoStartup(true)
            .idempotentConsumer(header("CamelFileName"),
                MemoryIdempotentRepository.memoryIdempotentRepository(10000))
            .log("Unzipping ${file:path}");




//
//            .split(new ZipSplitter())
//            .streaming()
//            .log("Unzipped ${file:name}")
//            .end();
//            .to("direct:terrain50data");

//        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
//
//        JAXBContext jaxbContext = JAXBContext.newInstance(
//            uk.os.namespaces.open.rivers._1.ObjectFactory.class,
//            inspire.x.specification.gmlas.hydronetwork._3.ObjectFactory.class,
//            uk.os.namespaces.open.rivers._1.WatercourseLinkType.class,
//            uk.os.namespaces.product._1.ObjectFactory.class,
//            net.opengis.gml._3.ObjectFactory.class
//        );
//
//        jaxbDataFormat.setContext(jaxbContext);
//
//        Namespaces namespaces = new Namespaces()
//            .add("os", "http://namespaces.os.uk/product/1.0")
//            .add("river", "http://namespaces.os.uk/Open/Rivers/1.0");
//
//        from("file:" + OPENRIVERS_DATA + "&noop=true")
//            .multicast()
//            .to("seda:watercourselinks", "seda:hydronodes");
//
//        from("seda:watercourselinks")
//            .routeId("watercourselinks")
//            .autoStartup(false)
//            .split(xpath("//river:WatercourseLink", namespaces))
//            .streaming()
//            .unmarshal(jaxbDataFormat)
//            .process(watercourseLinkProcessor)
//            .to("jpa:WatercourseLink");
//
//        from("seda:hydronodes")
//            .routeId("hydronodes")
//            .autoStartup(false)
//            .split(xpath("//river:HydroNode", namespaces))
//            .streaming()
//            .unmarshal(jaxbDataFormat)
//            .process(hydroNodeProcessor)
//            .to("jpa:HydroNode");
    }
}
