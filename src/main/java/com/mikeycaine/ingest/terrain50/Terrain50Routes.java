package com.mikeycaine.ingest.terrain50;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Terrain50Routes extends RouteBuilder {

    private final Terrain50DataProcessor terrain50DataProcessor;

    final static String TERR50_DATA = "E:\\Downloads\\terr50_gagg_gb\\terr50_gagg_gb\\data";

    @Override
    public void configure() throws Exception {
        from("file:" + TERR50_DATA + "?noop=true&recursive=true")
            .routeId("Terrain50")
            .autoStartup(false)
            .idempotentConsumer(
                header("CamelFileName"),
                MemoryIdempotentRepository.memoryIdempotentRepository(10000))
            .split(new ZipSplitter())
            .streaming()
            .filter(simple("${header.zipFileName} endsWith '.asc'"))
            .log("Unzipped ${file:name}")
            .process(terrain50DataProcessor)
            .to("jpa:Terrain50Grid");
    }
}
