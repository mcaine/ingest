package com.mikeycaine.ingest.terrain50;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

//import static org.apache.camel.component.file.FileConstants.FILE_NAME;

@Service
@Slf4j
public class Terrain50DataProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String fileName = in.getHeader("CamelFileNameOnly", String.class);
        log.info("File name is " + fileName);

    }
}
