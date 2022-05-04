package com.mikeycaine.ingest;

import net.opengis.gml._3.CoordinatesType;
import net.opengis.gml._3.LinearRingType;
import org.locationtech.jts.geom.*;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    protected static GeometryFactory newGeometryFactory() {
      return new GeometryFactory(new PrecisionModel(), 27700);
    }
    public static LinearRing convertLinearRingType(LinearRingType lrt) {
        CoordinatesType coordinatesType = lrt.getCoordinates();
        List<Double> positions = lrt.getPosList().getValue();
        int nCoords = positions.size() / 2;

        Coordinate[] coordinates = new Coordinate[nCoords];
        for (int i = 0; i < nCoords; ++i) {
            coordinates[i] = new Coordinate(lrt.getPosList().getValue().get(2 * i), lrt.getPosList().getValue().get(2 * i + 1));
        }

        return newGeometryFactory().createLinearRing(coordinates);
    }

    public static Polygon createPolygon(LinearRingType exterior, List<LinearRingType> interiors) {
        LinearRing exteriorLinearRing = Util.convertLinearRingType(exterior);
        List<LinearRing> interiorPolygons = interiors.stream().map(Util::convertLinearRingType).collect(Collectors.toList());
        LinearRing[] interiorPolygonsArray = interiorPolygons.toArray(new LinearRing[interiorPolygons.size()]);
        return newGeometryFactory().createPolygon(exteriorLinearRing, interiorPolygonsArray);
    }
}
