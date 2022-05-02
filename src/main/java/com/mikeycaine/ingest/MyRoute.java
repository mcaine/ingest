package com.mikeycaine.ingest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import java.util.Iterator;

@Component
public class MyRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {
        //from("timer:foo").to("log:bar");

        //ZipFileDataFormat zipFile = new ZipFileDataFormat();
        //zipFile.setUsingIterator("true");

        //JaxbDataFormat jaxbDataFormat = new JaxbDataFormat("eu.europa.ec.inspire.schemas.base._3");
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat("eu.europa.ec.inspire.schemas.base._3");
        //JAXBContext context = JAXBContext.newInstance("eu.europa.ec.inspire.schemas.base._3");


        JAXBContext jaxbContext = JAXBContext.newInstance(
            eu.europa.ec.inspire.schemas.base._3.SpatialDataSetType.class,
            eu.europa.ec.inspire.schemas.base._3.SpatialDataSetPropertyType.class,
            eu.europa.ec.inspire.schemas.au._4.AdministrativeUnitType.class
        );
        jaxbDataFormat.setContext(jaxbContext);
        //jaxbDataFormat.setSchema("file:src/main/xsd/AdministrativeUnits.xsd");

        //jaxbDataFormat.setContextPath("eu.europa.ec.inspire.schemas.base._3");


        from("file:E:\\Downloads\\bdline_gml3_gb\\Data?fileName=INSPIRE_AdministrativeUnit.gml&noop=true")
 //               .convertBodyTo(String.class)
//                .unmarshal()
//                .zipFile()
                .unmarshal(jaxbDataFormat)
                //                .unmarshal(zipFile)
                //.split(bodyAs(Iterator.class)).streaming()
                //.jaxb("eu.europa.ec.inspire.schemas.base._3")
                .to("log:inspire")
;



//                .unmarshal(zipFile)
//                .split(bodyAs(Iterator.class)).streaming()
//                .process(new UnZippedMessageProcessor())
//                .end();
    }


}
