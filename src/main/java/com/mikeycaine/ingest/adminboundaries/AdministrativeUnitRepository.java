package com.mikeycaine.ingest.adminboundaries;

import com.mikeycaine.ingest.adminboundaries.AdministrativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, String> {
}
