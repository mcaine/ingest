package com.mikeycaine.ingest.terrain50;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class GridReader {
    final private String body;
    final private String gridSquare;

    public GridReader(String gridSquare, String body) {
        this.gridSquare = gridSquare;
        this.body = body;
    }

    public GridData result() {
        int nRows = 0;
        int nCols = 0;
        int xllCorner = 0;
        int yllCorner = 0;
        int cellSize = 0;
        int rowIdx = 0;

        Double[][] terrainData = null;

        try (Scanner scanner = new Scanner(body)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("ncols")) {
                    nCols = Integer.parseInt(line.substring(6));
                } else if (line.startsWith("nrows")) {
                    nRows = Integer.parseInt(line.substring(6));
                } else if (line.startsWith("xllcorner")) {
                    xllCorner = Integer.parseInt(line.substring(10));
                } else if (line.startsWith("yllcorner")) {
                    yllCorner = Integer.parseInt(line.substring(10));
                } else if (line.startsWith("cellsize")) {
                    cellSize = Integer.parseInt(line.substring(9));
                } else {
                    if (!line.isEmpty() && nCols > 0 && nRows > 0) {
                        if (terrainData == null) {
                            terrainData = new Double[nRows][nCols];
                        }
                        Scanner dataScanner = new Scanner(line);
                        for (int colIdx = 0; colIdx < nCols; ++colIdx) {
                            Double value = dataScanner.nextDouble();
                            terrainData[rowIdx][colIdx] = value;
                        }
                    }
                    ++rowIdx;
                }
            }
        }

        if (rowIdx != nRows) {
            throw new RuntimeException("Didn't read correct number of rows");
        }

        return new GridData(gridSquare, nRows, nCols, xllCorner, yllCorner, cellSize, terrainData);
    }
}
