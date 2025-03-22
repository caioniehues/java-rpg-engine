package io.cote.chatdm.oracle.repository;

import io.cote.chatdm.oracle.Oracle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


class OracleParser {
    private static final Logger logger = LoggerFactory.getLogger(OracleParser.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    Oracle parseOracleFile(String filename, InputStream inputStream) {
        Map<String, Object> jsonContent = null;
        try {
            jsonContent = objectMapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            logger.error("Error parsing JSON file '{}'", filename, e);
        }

        String name;
        LinkedHashMap<String, String> metadata = new LinkedHashMap<>();

        if (jsonContent != null && jsonContent.containsKey("metadata")) {
            Map<String, Object> jsonMetadata = (Map<String, Object>) jsonContent.get("metadata");
            jsonMetadata.forEach((key, value) -> metadata.put(key, value.toString()));
            name = metadata.getOrDefault("name", extractNameFromFilename(filename));
        } else {
            name = extractNameFromFilename(filename);
        }

        List<String> results = new ArrayList<>();
        if (jsonContent != null && jsonContent.containsKey("results")) {
            for (Object result : (List<Object>) jsonContent.get("results")) {
                results.add(result.toString());
            }
        }

        logger.info("Loaded oracle '{}' with {} results", name, results.size());
        return new Oracle(name, metadata, results);
    }

    private String extractNameFromFilename(String filename) {
        return filename.replaceAll("^chatdm\\.oracle\\.|\\.json$", "");
    }
}