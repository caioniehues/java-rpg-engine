package io.cote.chatdm.oracle.repository;

import io.cote.chatdm.oracle.Oracle;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.*;

@Repository
public class ClassPathOracleRepository implements OracleRepository {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathOracleRepository.class);
    private final OracleParser parser = new OracleParser();

    private final Map<String, Oracle> oracles = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Loads all oracles found in the classpath. Intended to be used at startup to populare the oracles.
     * This will replace any existing oracles
     * in this repository that have already been loaded.
     */
    @PostConstruct
    void loadAllOracles() {
        try {
            var resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:oracle/chatdm.oracle.*.yml");
            for (Resource resource : resources) {
                logger.debug("Loading oracle from resource: {}", resource);
                try (InputStream is = resource.getInputStream()) {
                    Oracle oracle = parser.parseOracleFile(resource.getFilename(), is);
                    oracles.put(oracle.name(), oracle);
                    logger.debug("Loaded oracle '{}'", oracle.name());
                } catch (Exception e) {
                    logger.error("Error loading oracle from resource {}", resource.getFilename(), e);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load oracle files from classpath", e);
        }
    }

    @Override
    public Set<String> findAllNames() {
        return Set.copyOf(oracles.keySet() );
    }

    @Override
    public Oracle findByName(String name) {
        return oracles.get(name);
    }

    @Override
    public boolean existsByName(String name) {
        return oracles.containsKey(name);
    }

    @Override
    public List<Oracle> findAll() {
        // TK should this be List.of? Seemed like an estoric reason to use it.
        return List.copyOf(oracles.values());
    }
}
