package com.mikeycaine.ingest;

import eu.europa.ec.inspire.schemas.au._4.AdministrativeUnitType;
import eu.europa.ec.inspire.schemas.gn._4.GeographicalNamePropertyType;
import eu.europa.ec.inspire.schemas.gn._4.SpellingOfNamePropertyType;
import lombok.extern.slf4j.Slf4j;
import net.opengis.gml._3.*;
import org.apache.camel.Exchange;
import org.isotc211._2005.gmd.CountryPropertyType;
import org.isotc211._2005.gmd.LocalisedCharacterStringPropertyType;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class AdministrativeUnitProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        AdministrativeUnitType administrativeUnit
                = exchange.getIn().getBody(AdministrativeUnitType.class);


        //log.info("Administrative Unit SRS Name: " + administrativeUnit.getGeometry().getMultiSurface().getSrsName());

        String id = administrativeUnit.getId();
        MultiSurfacePropertyType geometry = administrativeUnit.getGeometry();
        List<ReferenceType> admins = administrativeUnit.getAdministeredBy();
        List<GeographicalNamePropertyType> geoNames = administrativeUnit.getName();
        Stream<String> spellingStream = geoNames.stream().flatMap(gn -> gn.getGeographicalName().getSpelling().stream()).map(spelling -> spelling.getSpellingOfName().getText());
        String spelling = spellingStream.collect(Collectors.joining(", "));
        List<LocalisedCharacterStringPropertyType> nationalLevelNames = administrativeUnit.getNationalLevelName();
        String level = nationalLevelNames.stream().map(lcs -> lcs.getLocalisedCharacterString().getValue()).collect(Collectors.joining(", "));
        String country = administrativeUnit.getCountry().getCountry().getCodeListValue();
        String nationalCode = administrativeUnit.getNationalCode();


        log.info(id + " : "+ country + " " + spelling + " " + level + " " + nationalCode);


        //List<SurfacePropertyType> multi = geometry.getMultiSurface().getSurfaceMember();
        Stream<PolygonPatchType> patches = geometry.getMultiSurface().getSurfaceMember().stream()
                .filter(spt -> spt.getAbstractSurface().getDeclaredType().equals(SurfaceType.class))
                .flatMap(spt -> {
                    //JAXBElement<? extends AbstractSurfaceType> el = spt.getAbstractSurface();
                    SurfaceType surface = (SurfaceType)spt.getAbstractSurface().getValue();
                    return surface.getPatches().getValue().getAbstractSurfacePatch().stream().map(JAXBElement::getValue)
                            .filter(patch -> patch.getClass().equals(PolygonPatchType.class))
                            .map(patch -> {
                                PolygonPatchType polygonPatchType = (PolygonPatchType)patch;
                                return polygonPatchType;
                                //log.info(polygonPatchType.toString());
                            });

        });

        List<Polygon> polygons = patches.map(p -> {
            //log.info(p.getExterior().getAbstractRing().getDeclaredType().toString() );

//            if(!p.getExterior().getAbstractRing().getDeclaredType().equals(LinearRingType.class)) {
//                throw new RuntimeException("YOUR MUM");
//            }

            LinearRingType exterior = (LinearRingType) p.getExterior().getAbstractRing().getValue();
            //LinearRing exteriorLinearRing = Util.convertLinearRingType(exterior);

            List<LinearRingType> interiors = p.getInterior().stream().filter(obg -> obg.getAbstractRing().getDeclaredType().equals(LinearRingType.class))
                            .map(el -> (LinearRingType)el.getAbstractRing().getValue()).collect(Collectors.toList());

            //List<LinearRing> interiorPolygons = interiors.stream().map(interior -> Util.convertLinearRingType(interior)).collect(Collectors.toList());
            //LinearRing[] interiorPolygonsArray = interiorPolygons.toArray(new LinearRing[interiorPolygons.size()]);

            return Util.createPolygon(exterior, interiors);

//            if (!interiors.isEmpty()) {
//                log.info("INTERIOR of " + spelling + " HAS " + interiors.size());
//            }
        }).collect(Collectors.toList());

        MultiPolygon multiPolygon = Util.createMultiPolygon(polygons);

        log.info(polygons.size() + " polygons");



//        Optional<MultiSurfacePropertyType> multiSurfacePropertyTypeOptional = administrativeUnit.getRest().stream()
//                .filter(el -> el.getName().getLocalPart().equals("geometry"))
//                        .map(el -> (MultiSurfacePropertyType)el.getValue()).findAny();
//
//
//
//        Optional<GeographicalNamePropertyType> geographicalNamePropertyTypeOptional = administrativeUnit.getRest().stream()
//                .filter(el -> el.getName().getLocalPart().equals("name"))
//                .map(el -> (GeographicalNamePropertyType)el.getValue()).findAny();
//
//        Optional<CountryPropertyType> countryPropertyTypeOptional = administrativeUnit.getRest().stream()
//                .filter(el -> el.getName().getLocalPart().equals("country"))
//                .map(el -> (CountryPropertyType)el.getValue()).findAny();
//
//        multiSurfacePropertyTypeOptional.ifPresent(ms -> log.info("Multisurface SRS" + ms.getMultiSurface().getSrsName()));
//        geographicalNamePropertyTypeOptional.ifPresent(gn -> log.info("Geo name " + gn.getGeographicalName().getSpelling().stream().map(spelling -> spelling.getSpellingOfName().getText()).collect(Collectors.joining(","))));
//        countryPropertyTypeOptional.ifPresent(cp -> log.info("Country " + cp.getCountry().getCodeListValue()));
//
//
//
//
//        administrativeUnit.getRest().forEach(el -> {
//            log.info(el.getName().toString());
//            if (el.getName().getLocalPart().equals("country")) {
//                log.info("Found the country");
//                Object value = el.getValue();
//                log.info(value.toString());
//            }
//        });
    }
}
