package io.cote.chatdm.oracle;

import java.util.*;

/**
 * A simplified Oracle class that stores a name and a list of entries. Provides functionality to get random entries from
 * the list.
 */
public class Oracle {
    private final String name;
    private final String description;
    private final Map<String, String> metadata;
    private final List<String> results;
    private final Random random;

    /**
     * Creates a new Oracle with the given name and entries.
     *
     * @param name    The name of the oracle
     * @param entries The list of possible results
     */
    public Oracle(String name, LinkedHashMap<String, String> metadata, List<String> results) {
        this.name = name;
        this.description = metadata.get("description");
        this.metadata = metadata;
        this.results = new ArrayList<>(results); // Create a defensive copy
        this.random = new Random();
    }

    /**
     * Gets the name of this oracle.
     *
     * @return The oracle name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of one of the Oracle's metadata fields.
     * <p>
     * The metadata fields should have at least a name. All other fields are optional and arbitrary.
     * <p>
     * An example:
     *
     * {@snippet lang = "yaml":
     * metadata:
     *   name: "NPC_Motivations"
     *   description: "Use answers from this oracle to determine what drives an NPC's actions, their motivations"
     *   author: "Claude"
     *   version: 1.0
     *   category: "NPCs"
     *}
     *
     * @param name of metadata to get
     * @return value of metadata or null if none.
     */
    public String getMetadata(String name) {
        return metadata.get(name);
    }

    public Map getAllMetadata() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(metadata));
    }

    /**
     * Returns a description of the oracle that describes how it can be used
     * @return the description, can be any length.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Gets a random entry from this oracle.
     *
     * @return A randomly selected entry
     */
    public String getRandomResult() {
        if (results.isEmpty()) {
            return "No results available.";
        }
        return results.get(random.nextInt(results.size()));
    }

    /**
     * Gets the number of results in this oracle.
     *
     * @return The number of results
     */
    public int getResultsCount() {
        return results.size();
    }
}
