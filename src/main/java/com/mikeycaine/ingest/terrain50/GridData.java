package com.mikeycaine.ingest.terrain50;

import lombok.Getter;

import java.util.Scanner;

public class GridData {
    @Getter
    final private String gridSquare;
    @Getter
    final private int nCols;
    @Getter
    final private int nRows;
    @Getter
    final private int yllCorner;
    @Getter
    final private int xllCorner;
    @Getter
    final private int cellsize;
    @Getter
    final private float[][] terrainData;

    public GridData(String gridSquare, int nRows, int nCols, int xllCorner, int yllCorner, int cellSize, float[][] terrainData) {
        this.gridSquare = gridSquare;
        this.nRows = nRows;
        this.nCols = nCols;
        this.xllCorner = xllCorner;
        this.yllCorner = yllCorner;
        this.cellsize = cellSize;
        this.terrainData = terrainData;
    }

    public static GridData fromASC(String gridSquare, String ascString) {
        int nRows = 0;
        int nCols = 0;
        int xllCorner = 0;
        int yllCorner = 0;
        int cellSize = 0;
        int rowIdx = 0;

        float[][] data = null;

        try (Scanner scanner = new Scanner(ascString)) {
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
                        if (data == null) {
                            data = new float[nRows][nCols];
                        }
                        Scanner dataScanner = new Scanner(line);
                        for (int colIdx = 0; colIdx < nCols; ++colIdx) {
                            data[rowIdx][colIdx] = dataScanner.nextFloat();
                        }
                    }
                    ++rowIdx;
                }
            }
        }

        if (rowIdx != nRows) {
            throw new RuntimeException("Didn't read correct number of rows");
        }

        return new GridData(gridSquare, nRows, nCols, xllCorner, yllCorner, cellSize, data);
    }
}
