package com.mikeycaine.ingest.terrain50;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Found 2858 terrain tiles
// Max height for tile NN17: HeightAtPosition(x=216650, y=771250, height=1345.0, gridSquare=NN17)
// Max height for tile NY20: HeightAtPosition(x=321500, y=507200, height=974.2, gridSquare=NY20)
// Max height for tile SH65: HeightAtPosition(x=260950, y=354350, height=1080.4, gridSquare=SH65)

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
    private float[] terrainData;

    public Terrain50Grid() {
    }

    public Terrain50Grid(String gridSquare, int nRows, int nCols, int xllCorner, int yllCorner, int cellSize, float[] terrainData) {
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
        int dataIdx = 0;

        float[] data = null;

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
                            data = new float[nRows * nCols];
                        }

                        Scanner dataScanner = new Scanner(line);
                        int itemsInThisRow = 0;
                        while(dataScanner.hasNextFloat()) {
                            float h = dataScanner.nextFloat();
                            ++itemsInThisRow;
                            data[dataIdx] = h;
                            ++dataIdx;
                        }
                        if (itemsInThisRow != nCols) {
                            throw new RuntimeException("Expected " + nCols + " items in row, found " + itemsInThisRow);
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

    public Stream<HeightAtPosition> heights() {
        return IntStream.range(0, nRows).mapToObj(Integer::valueOf)
                .flatMap(rowIdx -> IntStream.range(0, nCols).mapToObj(
                    colIdx -> {
                        int idx = rowIdx * nCols + colIdx;
                        return new HeightAtPosition(xllCorner + colIdx * cellsize, yllCorner + (nRows - rowIdx - 1) * cellsize, terrainData[idx], gridSquare);
                    }
                ));
    }

    public Optional<HeightAtPosition> maxHeightOpt() {
        return this.heights().sorted(Comparator.reverseOrder()).findFirst();
    }
}
