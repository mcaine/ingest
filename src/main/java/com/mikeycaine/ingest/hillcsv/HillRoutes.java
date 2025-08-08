package com.mikeycaine.ingest.hillcsv;

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
public class HillRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // http://www.hills-database.co.uk/hillcsv.zip

        from("file:E:\\Downloads\\hillcsv\\?fileName=DoBIH_v17_4.csv&noop=true")
            .routeId("British Hills Database")
            .autoStartup(false)
            .unmarshal().csv()
            .split(body())
            .process(exchange -> {
                    Message in = exchange.getIn();
                    Map fields =  in.getBody(LinkedHashMap.class);
                    Hill hill = Hill.fromHashMap(fields);
                    in.setBody(hill, Hill.class);
                    log.info(hill.getName());
                })
            .to("jpa:Hill");
    }
}
