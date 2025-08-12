package com.mikeycaine.ingest.opentoid;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.mikeycaine.ingest.RecordUtils.*;

// create table osopentoid (toid character varying(32) primary key, version_number smallint, version_date character varying(32) ,source_product smallint, easting real, northing real);

@Entity
@Table(name="osopentoid")
@Slf4j
public class OSOpenTOID {

    @Id @Getter @Setter
    private String toid;

    @Getter @Setter
    private int versionNumber;

    @Getter @Setter
    private String versionDate;

    @Getter @Setter
    private int  sourceProduct;

    @Getter @Setter
    private float easting;

    @Getter @Setter
    private float northing;

    public static OSOpenTOID fromMap(Map<String, String> record) {
        OSOpenTOID toid = new OSOpenTOID();

        // wtf
        String theToid = record.get("\uFEFFTOID");
        if (theToid == null) {
            theToid = record.get("TOID");
        }
//        log.info("TOID=" + theToid);

//        for (Map.Entry<String, String> entry : record.entrySet()) {
//            log.info("RECORD " + entry.getKey() + " = " + entry.getValue());
//            entry.getKey().chars().forEach(c -> log.info(Character.toString(c) + " " + c));
//        }

        toid.toid = theToid;
        toid.versionNumber = fieldAsInteger(record, "VERSION_NUMBER");
        toid.versionDate = record.get("VERSION_DATE");

        String sourceProduct = record.get("SOURCE_PRODUCT");
        switch (sourceProduct) {
            case "OS MasterMap Topography Layer" -> toid.sourceProduct = 1;
            case "OS MasterMap Sites Layer" -> toid.sourceProduct = 2;
            case "OS MasterMap Highways Network" -> toid.sourceProduct = 3;
            default -> throw new RuntimeException("Unrecognised source product");
        }

        toid.easting = fieldAsFloat(record, "EASTING");
        toid.northing = fieldAsFloat(record, "NORTHING");

        return toid;
    }
}
