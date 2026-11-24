package com.mikeycaine.ingest.terrain50;

import com.mikeycaine.ingest.config.FileEndpointUris;
import com.mikeycaine.ingest.config.IngestInputProperties;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Terrain50Routes extends RouteBuilder {

    private final Terrain50DataProcessor terrain50DataProcessor;
    private final IngestInputProperties input;

    @Override
    public void configure() throws Exception {
        IngestInputProperties.Terrain50 terrain50 = input.terrain50();
        from(FileEndpointUris.directoryOnly(terrain50.directory(),
                Map.of("noop", true, "recursive", true)))
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
