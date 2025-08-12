package com.mikeycaine.ingest;

import java.util.Map;
import java.util.Optional;

public class RecordUtils {
    public static Float fieldAsFloat(Map<String, String> record, String fieldName) {
        return Optional.of(record.get(fieldName)).map(Float::valueOf).orElse(null);
    }

    public static Integer fieldAsInteger(Map<String, String> record, String fieldName) {
        return Optional.of(record.get(fieldName)).map(Integer::valueOf).orElse(null);
    }
}
