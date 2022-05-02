package com.mikeycaine.ingest;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;

@Component
@RequiredArgsConstructor
public class MyRoute2 extends RouteBuilder {

    private final AdministrativeUnitProcessor administrativeUnitProcessor;

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        JAXBContext jaxbContext = JAXBContext.newInstance(eu.europa.ec.inspire.schemas.au._4.AdministrativeUnitType.class);
        jaxbDataFormat.setContext(jaxbContext);

        Namespaces namespaces = new Namespaces()
                .add("au", "http://inspire.ec.europa.eu/schemas/au/4.0");

        from("file:E:\\Downloads\\bdline_gml3_gb\\Data?fileName=INSPIRE_AdministrativeUnit.gml&noop=true")
                .split(xpath("//au:AdministrativeUnit", namespaces))
                .streaming()
                .unmarshal(jaxbDataFormat)
                .process(administrativeUnitProcessor)
                .end();
    }


}
