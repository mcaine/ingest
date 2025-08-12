package com.mikeycaine.ingest.opentoid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OSOpenTOIDRepository extends JpaRepository<OSOpenTOID, String> {}