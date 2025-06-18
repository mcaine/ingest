package com.mikeycaine.ingest.adminboundaries;

import com.mikeycaine.ingest.Util;
import eu.europa.ec.inspire.schemas.au._4.AdministrativeUnitType;
import eu.europa.ec.inspire.schemas.gn._4.GeographicalNamePropertyType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.opengis.gml._3.*;
import org.isotc211._2005.gmd.LocalisedCharacterStringPropertyType;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;


import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminUnitService {

    final private static int SRID = 27700;

    final private AdministrativeUnitRepository repo;

    void persistAdminstrativeUnit(AdministrativeUnitType administrativeUnitType) {
        AdministrativeUnit administrativeUnit = convertToOurType(administrativeUnitType);

        repo.save(administrativeUnit);
    }

    private AdministrativeUnit convertToOurType(AdministrativeUnitType administrativeUnitType) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);

        String id = administrativeUnitType.getId();
        MultiSurfacePropertyType geometry = administrativeUnitType.getGeometry();
        List<ReferenceType> admins = administrativeUnitType.getAdministeredBy();
        List<GeographicalNamePropertyType> geoNames = administrativeUnitType.getName();
        Stream<String> spellingStream = geoNames.stream().flatMap(gn -> gn.getGeographicalName().getSpelling().stream()).map(spelling -> spelling.getSpellingOfName().getText());
        String name = spellingStream.collect(Collectors.joining(", "));
        List<LocalisedCharacterStringPropertyType> nationalLevelNames = administrativeUnitType.getNationalLevelName();
        String level = nationalLevelNames.stream().map(lcs -> lcs.getLocalisedCharacterString().getValue()).collect(Collectors.joining(", "));
        String country = administrativeUnitType.getCountry().getCountry().getCodeListValue();
        String nationalCode = administrativeUnitType.getNationalCode();

        log.info(id + " : " + country + " " + name + " (" + level + ") " + nationalCode);

        List<Polygon> polygons = getPolygonList(geometry);

        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(GeometryFactory.toPolygonArray(polygons));

        AdministrativeUnit administrativeUnit = new AdministrativeUnit();
        administrativeUnit.setId(id);
        administrativeUnit.setName(name);
        administrativeUnit.setBoundary(multiPolygon);
        administrativeUnit.setCode(nationalCode);
        administrativeUnit.setLevel(level);
        return administrativeUnit;
    }

    private List<Polygon> getPolygonList(MultiSurfacePropertyType geometry) {

        Stream<PolygonPatchType> patches = geometry.getMultiSurface().getSurfaceMember().stream()
                .filter(spt -> spt.getAbstractSurface().getDeclaredType().equals(SurfaceType.class))
                .flatMap(spt -> {
                    SurfaceType surface = (SurfaceType) spt.getAbstractSurface().getValue();
                    return surface.getPatches().getValue().getAbstractSurfacePatch().stream().map(JAXBElement::getValue)
                        .filter(patch -> patch.getClass().equals(PolygonPatchType.class))
                        .map(patch -> {
                            PolygonPatchType polygonPatchType = (PolygonPatchType) patch;
                            return polygonPatchType;
                        });
                });

        List<Polygon> polygons = patches.map(p -> {
            LinearRingType exterior = (LinearRingType) p.getExterior().getAbstractRing().getValue();
            List<LinearRingType> interiors = p.getInterior().stream().filter(obg -> obg.getAbstractRing().getDeclaredType().equals(LinearRingType.class))
                    .map(el -> (LinearRingType) el.getAbstractRing().getValue()).collect(Collectors.toList());
            return Util.createPolygon(exterior, interiors);
        }).collect(Collectors.toList());

        return polygons;
    }

    public AdministrativeUnit administrativeUnitById(String id) {
        return repo.getById(id);
    }
}
