package com.mikeycaine.ingest.terrain50;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.arcgrid.ArcGridFormat;
import org.geotools.gce.arcgrid.ArcGridFormatFactory;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.hsqldb.lib.StringInputStream;
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

        GridReader gridReader = new GridReader(gridSquare, messageBody.get("ASC"));
        GridData gridData = gridReader.result();
        if ("NN17".equals(gridSquare)) {// Ben Nevis
            log.info("Ben Nevis");
            for (int rowIdx = 0; rowIdx < gridData.getNRows(); ++rowIdx)
                for (int colIdx = 0; colIdx < gridData.getNCols(); ++colIdx) {
                    int y = gridData.getYllCorner() + (200 - rowIdx) * gridData.getCellsize();
                    int x = gridData.getXllCorner() + colIdx * gridData.getCellsize();
                    log.info("x = " + x + ", y = " + y + ", height = " + gridData.getTerrainData()[rowIdx][colIdx]);

                }
        }
    }
}
