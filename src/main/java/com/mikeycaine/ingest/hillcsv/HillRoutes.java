package com.mikeycaine.ingest.hillcsv;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

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
                    Map<String, String> fields =  in.getBody(LinkedHashMap.class);
                    Hill hill = Hill.fromHashMap(fields);
                    in.setBody(hill, Hill.class);
                    log.info(hill.getName());
                })
            .to("jpa:Hill");
    }
}
