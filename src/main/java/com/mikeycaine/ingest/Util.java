package com.mikeycaine.ingest;

import net.opengis.gml._3.*;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Util {
    protected static GeometryFactory newGeometryFactory() {
      return new GeometryFactory(new PrecisionModel(), 27700);
    }
    public static LinearRing convertLinearRingType(LinearRingType lrt) {
        net.opengis.gml._3.CoordinatesType coordinatesType = lrt.getCoordinates();
        DirectPositionListType dplt = lrt.getPosList();
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

    public static LineString convertLineString(LineStringType lst) {
        DirectPositionListType dplt = lst.getPosList();
        if (dplt == null) {
            throw new RuntimeException("No pos list");
        }
        List<Double> values = dplt.getValue();
        int nCoords = values.size() / 2;
        Coordinate[] coordinates = new Coordinate[nCoords];
        for (int i = 0; i < nCoords; ++i) {
            coordinates[i] = new Coordinate(values.get(2 * i), values.get(2 * i + 1));
        }
        return newGeometryFactory().createLineString(coordinates);
    }

    public static Point pointFrom(Double x, Double y) {
        return newGeometryFactory().createPoint(new Coordinate(x,y));
    }

    public static Point pointFromDirectPositionOption(Optional<DirectPositionType> dpOption) {
        return dpOption.flatMap(dp -> Optional.ofNullable(dp.getValue())).map(dp -> {
            Double x = dp.get(0);
            Double y = dp.get(1);
            return Util.pointFrom(x,y);
        }).orElse(null);
    }

    public static String featureCollectionAsString(DefaultFeatureCollection featureCollection) throws IOException {
        FeatureJSON fj = new FeatureJSON();
        fj.setEncodeFeatureCRS(true);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            fj.writeFeatureCollection(featureCollection, os);
            return os.toString();
        }
    }

    public static CoordinateReferenceSystem crs(String crsName) throws FactoryException {
        return CRS.decode(crsName);
    }
}
