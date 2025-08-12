package com.mikeycaine.ingest.opentoid;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OSOpenTOIDRoutes extends RouteBuilder  {
    CsvDataFormat csv = new CsvDataFormat()
        .setLazyLoad(true)
        .setUseMaps(true);


    @Override
    public void configure() throws Exception {
        from("file:E:\\Downloads\\osopentoid_202508_csv_tq\\?fileName=osopentoid_202508_tq.csv&maxMessagesPerPoll=10000&noop=true")
 //       from("file:E:\\Downloads\\osopentoid_202508_csv_tq\\?fileName=short.csv&noop=true")
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
