package com.mikeycaine.ingest.adminboundaries;

import eu.europa.ec.inspire.schemas.au._4.AdministrativeUnitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdministrativeUnitProcessor implements Processor {

    private final AdminUnitService adminUnitService;
    @Override
    public void process(Exchange exchange) throws Exception {
        AdministrativeUnitType administrativeUnit
                = exchange.getIn().getBody(AdministrativeUnitType.class);

        adminUnitService.persistAdminstrativeUnit(administrativeUnit);
    }
}
