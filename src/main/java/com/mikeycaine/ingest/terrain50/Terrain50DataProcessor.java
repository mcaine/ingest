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
        String gridSquare = in.getHeader("zipFileName", String.class).substring(0,4).toUpperCase();
        String messageBody = in.getBody(String.class);

        in.setBody(Terrain50Grid.fromASC(gridSquare, messageBody), Terrain50Grid.class);
    }
}
