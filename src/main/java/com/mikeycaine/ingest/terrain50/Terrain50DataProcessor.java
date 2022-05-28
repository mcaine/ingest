package com.mikeycaine.ingest.terrain50;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class Terrain50DataProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String fileName = in.getHeader("CamelFileNameOnly", String.class);
        String gridSquare = in.getHeader("GRID_SQUARE", String.class);

        Map<String, String> messageBody = in.getBody(Map.class);
        String ascFileText = messageBody.get("ASC");

        in.setBody(Terrain50Grid.fromASC(gridSquare, ascFileText), Terrain50Grid.class);
    }
}
