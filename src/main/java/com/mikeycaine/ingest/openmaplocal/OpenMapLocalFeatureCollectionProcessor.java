package com.mikeycaine.ingest.openmaplocal;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import uk.os.namespaces.product._1.FeatureCollectionType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OpenMapLocalFeatureCollectionProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();

        String fileName = in.getHeader("CamelFileNameOnly", String.class);
        String gridSquare = fileName.substring(0,2).toUpperCase();
        in.setHeader("GRID_SQUARE", gridSquare);

        FeatureCollectionType fct = in.getBody(FeatureCollectionType.class);
        List featureList = fct.getFeatureMember().stream().map(fpt -> fpt.getAbstractFeature().getValue()).collect(Collectors.toList());
        in.setBody(featureList, List.class);
    }
}
