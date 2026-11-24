package com.mikeycaine.ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ingest.routes")
public record IngestRouteProperties(
        boolean adminBoundaries,
        boolean hills,
        boolean openToid,
        boolean openRivers,
        boolean openMapLocal,
        boolean terrain50) {
}
