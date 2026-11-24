package com.mikeycaine.ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ingest.input")
public record IngestInputProperties(
        AdminBoundaries adminBoundaries,
        Hills hills,
        OpenToid openToid,
        OpenRivers openRivers,
        OpenMapLocal openMapLocal,
        Terrain50 terrain50) {

    public record AdminBoundaries(String directory, String fileName) {}

    public record Hills(String directory, String fileName) {}

    public record OpenToid(String directory, String fileName) {}

    public record OpenRivers(String directory, String fileName) {}

    public record OpenMapLocal(String directory) {}

    public record Terrain50(String directory) {}
}
