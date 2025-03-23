package io.cote.chatdm.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class OracleParser {
    private static final Logger logger = LoggerFactory.getLogger(OracleParser.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * A text oracle is one result per line, using the filename for the oracle name.
     * @param filename The name of the oracle file
     * @param inputStream The InputStream for the oracle file
     * @return an Oracle
     * @throws IOException if there is a problem parsing the oracle file
     */
    Oracle parseOracleTxtFile(String filename, InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Read the lines from the InputStream
            List<String> results = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                results.add(line.trim());
            }
            // Extract the oracle name from the filename
            String oracleName = filename.replaceFirst("chatdm\\.oracle\\.", "").replaceFirst("\\.txt$", "");

            // Create metadata for the oracle
            Map<String, String> metadata = new HashMap<>();
            metadata.put("name", oracleName);
            metadata.put("description", String.format("An oracle to use when the phrase %s describes what you want to be inspired by.", oracleName));

            // Create the Oracle object
            Oracle oracle = new Oracle(oracleName, metadata, results);

            // Log the oracle creation
            logger.info("Loaded txt oracle '{}' with {} results", oracle.name(), results.size());
            return oracle;
        } catch (IOException e) {
            logger.error("Error reading oracle file '{}'", filename, e);
            throw e;
        }
    }

    /**
     * Parses a json formatted oracle file
     *
     * @param filename
     * @param inputStream
     * @return
     */
    Oracle parseOracleJsonFile(String filename, InputStream inputStream) {
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

        logger.info("Loaded json oracle '{}' with {} results", name, results.size());
        return new Oracle(name, metadata, results);
    }

    private String extractNameFromFilename(String filename) {
        return filename.replaceAll("^chatdm\\.oracle\\.|\\.json$", "");
    }
}