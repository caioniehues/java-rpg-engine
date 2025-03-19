package io.cote.chatdm.oracle;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * Service for accessing oracles.
 */
@Service
public class OracleService {
    private static final Logger logger = LoggerFactory.getLogger(OracleService.class);

    // Make sure key lookups are case insensitive.
    private final Map<String, Oracle> oracles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);;
    private final OracleReader oracleReader = new OracleReader();

    @Value("${oracle.external.directory:}")
    private String externalDirectory;


    /**
     * Gets the names of all available oracles.
     *
     * @return A set of oracle names
     */
    public Set<String> getOracleNames() {
        return oracles.keySet();
    }

    /**
     * Gets information about all available oracles.
     *
     * @return A map of oracle names to entry counts
     */
    public Map<String, Integer> getOracleInfo() {
        Map<String, Integer> info = new HashMap<>();
        for (Oracle oracle : oracles.values()) {
            info.put(oracle.getName(), oracle.getResultsCount());
        }
        return info;
    }

    /**
     * Checks if an oracle with the given name exists.
     *
     * @param oracleName The name of the oracle
     * @return true if the oracle exists, false otherwise
     */
    public boolean hasOracle(String oracleName) {
        return oracles.containsKey(oracleName);
    }

    /**
     * Returns the named oracle. The name will either defined in the yml file with the field {@code metadata.name}, or,
     * if that is not available, is the name of the file.
     *
     * @param oracleName name of the oracle to retrieve.
     * @return Oracle, or null if there is no Oracle with that name.
     */
    public Oracle getOracle(String oracleName) {
        return oracles.get(oracleName);
    }

    public Map<String, Oracle> getOracles() {
        return oracles;
    }

    @PostConstruct
    public void init() throws Exception {
        // Load oracles from classpath
        Map<String, Oracle> classpathOracles = oracleReader.loadFromClasspath();
        oracles.putAll(classpathOracles);
        logger.info("Loaded {} oracles from classpath", classpathOracles.size());

        // Load oracles from external directory if specified
        if (externalDirectory != null && !externalDirectory.isEmpty()) {
            Map<String, Oracle> externalOracles = oracleReader.loadFromDirectory(externalDirectory);
            oracles.putAll(externalOracles);
            logger.info("Loaded {} oracles from external directory: {}", externalOracles.size(), externalDirectory);
        }
    }
}
