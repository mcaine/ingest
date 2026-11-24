package com.mikeycaine.ingest.opentoid;

import com.mikeycaine.ingest.config.FileEndpointUris;
import com.mikeycaine.ingest.config.IngestInputProperties;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OSOpenTOIDRoutes extends RouteBuilder  {

    private final IngestInputProperties input;

    CsvDataFormat csv = new CsvDataFormat()
        .setLazyLoad(true)
        .setUseMaps(true);


    @Override
    public void configure() throws Exception {
        IngestInputProperties.OpenToid openToid = input.openToid();
        from(FileEndpointUris.withFileName(openToid.directory(), openToid.fileName(),
                Map.of("maxMessagesPerPoll", 10000, "noop", true)))
        // Use --ingest.input.open-toid.file-name=short.csv to override the input file.
            .routeId("OS Open TOID")
            .autoStartup(false)
            .unmarshal(csv)
            .split(body())
            .streaming()
            .process(exchange -> {
                Message in = exchange.getIn();
                Map<String, String> fields =  in.getBody(LinkedHashMap.class);
                OSOpenTOID toid = OSOpenTOID.fromMap(fields);
                in.setBody(toid, OSOpenTOID.class);
                //log.info(toid.getToid());
            })
            .to("jpa:OSOpenTOID");
    }
}
