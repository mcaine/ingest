package com.mikeycaine.ingest.hillcsv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.ProcessClause;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HillRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // http://www.hills-database.co.uk/hillcsv.zip

        from("file:E:\\Downloads\\hillcsv\\?fileName=DoBIH_v17_4.csv&noop=true")
            .autoStartup(true)
            .unmarshal().csv()
            .process(exchange -> {
                log.info("Reading Hills...");
                    Message in = exchange.getIn();
                    List<LinkedHashMap<String, String>> records = in.getBody(ArrayList.class);
                    List<Hill> hills = records.stream().map(Hill::fromHashMap).collect(Collectors.toList());
                    in.setBody(hills, List.class);
                })
            .split(body())
            .to("jpa:Hill");

    }
}
