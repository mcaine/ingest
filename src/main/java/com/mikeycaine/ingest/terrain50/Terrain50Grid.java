package com.mikeycaine.ingest.terrain50;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Scanner;

@Entity
@Table(name="terrain50grid")
public class Terrain50Grid {
    @Getter
    @Setter
    @Id
    private String gridSquare;

    @Getter
    @Setter
    private int nCols;

    @Getter
    @Setter
    private int nRows;

    @Getter
    @Setter
    private int xllCorner;

    @Getter
    @Setter
    private int yllCorner;

    @Getter
    @Setter
    private int cellsize;

    @Getter
    @Setter
    private float[][] terrainData;

    public Terrain50Grid() {
    }

    public Terrain50Grid(String gridSquare, int nRows, int nCols, int xllCorner, int yllCorner, int cellSize, float[][] terrainData) {
        this.gridSquare = gridSquare;
        this.nRows = nRows;
        this.nCols = nCols;
        this.xllCorner = xllCorner;
        this.yllCorner = yllCorner;
        this.cellsize = cellSize;
        this.terrainData = terrainData;
    }

    public static Terrain50Grid fromASC(String gridSquare, String ascString) {
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

        return new Terrain50Grid(gridSquare, nRows, nCols, xllCorner, yllCorner, cellSize, data);
    }
}
