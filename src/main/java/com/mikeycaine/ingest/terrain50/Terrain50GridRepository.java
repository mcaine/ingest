package com.mikeycaine.ingest.terrain50;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Terrain50GridRepository extends JpaRepository<Terrain50Grid, String> {
}
