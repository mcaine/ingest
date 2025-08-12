package com.mikeycaine.ingest.hillcsv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;

import static com.mikeycaine.ingest.RecordUtils.*;

@Entity
@Table(name="hill")
@ToString
@NoArgsConstructor
public class Hill {

    // Number
    @Id @Getter @Setter
    private Integer id;

    // Name
    @Getter @Setter
    private String name;

    // Parent (SMC)
    @Getter @Setter
    private String parentSMC;

    // Parent name (SMC)
    @Getter @Setter
    private String parentNameSMC;

    // Section
    @Getter @Setter
    private String section;

    // Region
    @Getter @Setter
    private String region;

    // Area
    @Getter @Setter
    private String area;

    // Island
    @Getter @Setter
    private String island;

    // Topo Section
    // County
    // Classification
    // Map 1:50k
    // Map 1:25k

    // Metres
    @Getter @Setter
    private Float heightMetres;

    // Feet
    @Getter @Setter
    private Float heightFeet;

    // Grid ref
    @Getter @Setter
    private String gridRef;

    // Grid ref 10
    @Getter @Setter
    private String gridRef10;


    // Drop
    // Col grid ref
    // Col height
    // Feature
    // Observations
    // Survey
    // Climbed
    // Country
    // County Top
    // Revision
    // Comments
    // Streetmap/MountainViews
    // Geograph
    // Hill-bagging

    // Xcoord
    @Getter @Setter
    private Float xCoord;

    // Ycoord
    @Getter @Setter
    private Float yCoord;

    // Latitude
    @Getter @Setter
    private Float latitude;

    // Longitude
    @Getter @Setter
    private Float longitude;

    // GridrefXY
    // _Section
    // Parent (Ma)
    // Parent name (Ma)
    // MVNumber
    // Ma
    // Ma=
    // Hu
    // Hu=
    // Tu
    // Sim
    // 5
    // M
    // MT
    // F
    // C
    // G
    // D
    // DT
    // Hew
    // N
    // Dew
    // DDew
    // HF
    // 4
    // 3
    // 2
    // 1
    // 0
    // W
    // WO
    // B
    // Sy
    // Fel
    // CoH
    // CoH=
    // CoU
    // CoU=
    // CoA
    // CoA=
    // CoL
    // CoL=
    // SIB
    // sMa
    // sHu
    // sSim
    // s5
    // s4
    // Mur
    // CT
    // GT
    // BL
    // Bg
    // Y
    // Cm
    // T100
    // xMT
    // xC
    // xG
    // xN
    // xDT
    // Dil
    // VL
    // A
    // Ca
    // Bin
    // O
    // Un
    public static Hill fromHashMap(Map<String, String> record) {
        Hill hill = new Hill();

        hill.id = Integer.valueOf(record.get("Number"));
        hill.name = record.get("Name");
        hill.parentSMC = record.get("Parent (SMC)");
        hill.parentNameSMC = record.get("Parent name (SMC)");
        hill.section = record.get("Section");
        hill.region = record.get("Region");
        hill.area = record.get("Area");
        hill.island = record.get("Island");
        hill.heightMetres = fieldAsFloat(record, "Metres");
        hill.heightFeet = fieldAsFloat(record, "Feet");
        hill.gridRef = record.get("Grid ref");
        hill.gridRef10 = record.get("Grid ref 10");
        hill.xCoord = fieldAsFloat(record, "Xcoord");
        hill.yCoord = fieldAsFloat(record, "Ycoord");
        hill.latitude = fieldAsFloat(record, "Latitude");
        hill.longitude = fieldAsFloat(record, "Longitude");

        return hill;
    }


}
