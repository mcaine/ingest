package com.mikeycaine.ingest.adminboundaries;

import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBoundariesRoute extends RouteBuilder {

    private final AdministrativeUnitProcessor administrativeUnitProcessor;

    @Override
    public void configure() throws Exception {

        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        jaxbDataFormat.setContext(JAXBContext.newInstance("eu.europa.ec.inspire.schemas.au._4"));

        Namespaces namespaces = new Namespaces()
            .add("au", "http://inspire.ec.europa.eu/schemas/au/4.0");

        from("file:E:\\Downloads\\bdline_gml3_gb\\Data?fileName=INSPIRE_AdministrativeUnit.gml&noop=true")
            .routeId("AdministrativeUnit")
            .autoStartup(false)
            .split(xpath("//au:AdministrativeUnit", namespaces))
            .streaming()
            .unmarshal(jaxbDataFormat)
            .process(administrativeUnitProcessor)
            .end();
    }
}
