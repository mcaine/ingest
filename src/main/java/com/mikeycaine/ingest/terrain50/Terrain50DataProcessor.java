package com.mikeycaine.ingest.terrain50;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.Map;

//import static org.apache.camel.component.file.FileConstants.FILE_NAME;

@Service
@Slf4j
public class Terrain50DataProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String fileName = in.getHeader("CamelFileNameOnly", String.class);
        String gridSquare = in.getHeader("GRID_SQUARE", String.class);
        log.info("Grid Square " + gridSquare);
        Map<String, String> messageBody = in.getBody(Map.class);
        log.info("Size is" + messageBody.size());

    }
}
