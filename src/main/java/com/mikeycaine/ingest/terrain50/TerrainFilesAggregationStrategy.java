package com.mikeycaine.ingest.terrain50;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

import java.util.HashMap;
import java.util.Map;

public class TerrainFilesAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Message newIn = newExchange.getIn();
        String camelFileName = newIn.getHeader("CamelFileName", String.class);
        String gridSquare = camelFileName.substring(0,4).toUpperCase();
        String zipFileName = newIn.getHeader("zipFileName", String.class);
        String newBody = newIn.getBody(String.class);
        String key = key(zipFileName);

        if (oldExchange == null) {
            Map<String, String> data = new HashMap<>();
            data.put(key, newBody);
            newIn.setBody(data);
            newIn.setHeader("GRID_SQUARE", gridSquare);
            return newExchange;
        } else {
            Message in = oldExchange.getIn();
            Map<String, String> data = in.getBody(HashMap.class);
            data.put(key, newBody);
            return oldExchange;
        }
    }

    public String key(String s) {
        if (s.endsWith(".asc.aux.xml")) {
            return "ASC_AUC_XML";
        } else if (s.endsWith(".xml")) {
            return "XML";
        } else if (s.endsWith(".asc")) {
            return "ASC";
        } else if (s.endsWith(".gml")) {
            return "GML";
        } else if (s.endsWith(".prj")) {
            return "PRJ";
        } else {
            throw new RuntimeException("Can't get key from " + s);
        }
    }
}
