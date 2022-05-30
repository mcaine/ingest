package com.mikeycaine.ingest.openrivers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatercourseLinkRepository extends JpaRepository<WatercourseLink, String> {

    List<WatercourseLink> findByName(String name);
}
