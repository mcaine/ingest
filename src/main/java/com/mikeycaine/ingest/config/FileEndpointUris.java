package com.mikeycaine.ingest.config;

import org.apache.camel.util.URISupport;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class FileEndpointUris {

    private FileEndpointUris() {
    }

    public static String withFileName(String directory, String fileName, Map<String, Object> options) {
        requireText(directory, "directory");
        requireText(fileName, "fileName");

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("fileName", fileName);
        parameters.putAll(options);
        return fileUri(directory, parameters);
    }

    public static String directoryOnly(String directory, Map<String, Object> options) {
        requireText(directory, "directory");
        return fileUri(directory, options);
    }

    private static String fileUri(String directory, Map<String, Object> options) {
        String baseUri = "file:" + directory.replace('\\', '/');
        try {
            return URISupport.appendParametersToURI(baseUri, options);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid file directory: " + directory, e);
        }
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing ingest input configuration: " + name);
        }
    }
}
