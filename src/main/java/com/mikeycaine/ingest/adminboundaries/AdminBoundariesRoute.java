package com.mikeycaine.ingest.adminboundaries;

import com.mikeycaine.ingest.config.FileEndpointUris;
import com.mikeycaine.ingest.config.IngestInputProperties;
import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.apache.camel.support.builder.Namespaces;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminBoundariesRoute extends RouteBuilder {

    private final AdminUnitService adminUnitService;
    private final IngestInputProperties input;

    @Override
    public void configure() throws Exception {

        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        jaxbDataFormat.setContext(JAXBContext.newInstance("eu.europa.ec.inspire.schemas.au._4"));

        Namespaces namespaces = new Namespaces()
            .add("au", "http://inspire.ec.europa.eu/schemas/au/4.0");

        IngestInputProperties.AdminBoundaries admin = input.adminBoundaries();
        from(FileEndpointUris.withFileName(admin.directory(), admin.fileName(), Map.of("noop", true)))
            .routeId("AdministrativeUnit")
            .autoStartup(false)
            .split().xtokenize("//au:AdministrativeUnit", 'i', namespaces)
            .streaming()
            .unmarshal(jaxbDataFormat)
            .aggregate(header("CamelFileName"), new GroupedBodyAggregationStrategy())
                .completionSize(500)
                .completionTimeout(5000)
                .bean(adminUnitService, "saveAdministrativeUnits")
            .end();
    }
}
