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
                .routeId("terrain50 unzip")
                .idempotentConsumer(header("CamelFileName"),
                        MemoryIdempotentRepository.memoryIdempotentRepository(10000))
                .log("Unzipping ${file:path}")
                .split(new ZipSplitter(), new TerrainFilesAggregationStrategy())
                .streaming()
                .log("Unzipped ${file:name}")
                .end()
                .to("direct:terrain50data");

        from("direct:terrain50data")
                .routeId("terrain50 data")
                .log("Processing data from ${file:path}")
                .process(terrain50DataProcessor)
                .to("jpa:Terrain50Grid");
    }
}
