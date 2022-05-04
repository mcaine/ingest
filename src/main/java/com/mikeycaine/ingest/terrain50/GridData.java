package com.mikeycaine.ingest.terrain50;

import lombok.Getter;

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
    final private Double[][] terrainData;

    public GridData(String gridSquare, int nRows, int nCols, int xllCorner, int yllCorner, int cellSize, Double[][] terrainData) {
        this.gridSquare = gridSquare;
        this.nRows = nRows;
        this.nCols = nCols;
        this.xllCorner = xllCorner;
        this.yllCorner = yllCorner;
        this.cellsize = cellSize;
        this.terrainData = terrainData;
    }
}
