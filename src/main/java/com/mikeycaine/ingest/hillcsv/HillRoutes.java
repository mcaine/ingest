package com.mikeycaine.ingest.hillcsv;

import com.mikeycaine.ingest.config.FileEndpointUris;
import com.mikeycaine.ingest.config.IngestInputProperties;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HillRoutes extends RouteBuilder {

    private final IngestInputProperties input;

    @Override
    public void configure() throws Exception {

        // http://www.hills-database.co.uk/hillcsv.zip

        IngestInputProperties.Hills hills = input.hills();
        from(FileEndpointUris.withFileName(hills.directory(), hills.fileName(), Map.of("noop", true)))
            .routeId("British Hills Database")
            .autoStartup(false)
            .unmarshal().csv()
            .split(body())
            .process(exchange -> {
                    Message in = exchange.getIn();
                    Map<String, String> fields =  in.getBody(LinkedHashMap.class);
                    Hill hill = Hill.fromHashMap(fields);
                    in.setBody(hill, Hill.class);
                    log.info(hill.getName());
                })
            .to("jpa:Hill");
    }
}
