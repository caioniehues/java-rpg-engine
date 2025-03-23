package io.cote.chatdm.oracle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/chatdm/api/oracle")
public class OracleController {

    private final OracleRepository oracleRepository;

    public OracleController(OracleRepository oracleRepository) {
        this.oracleRepository = oracleRepository;
    }

    /**
     * Gets all oracle names.
     */
    @GetMapping
    public List<String> getAllOracleNames() {
        return List.copyOf(oracleRepository.findAllNames());
    }

    /**
     * Gets detailed information about all oracles.
     */
    @GetMapping("/info")
    public Map<String, Integer> getOracleInfo() {
        Map<String, Integer> info = new HashMap<>();
        for (Oracle oracle : oracleRepository.findAll()) {
            info.put(oracle.name(), oracle.results().size());
        }
        return Map.copyOf(info);
    }

    /**
     * Retrieves the whole oracle.
     *
     * @param name of the oracle to get
     * @return the {@link Oracle}
     * @throws ResponseStatusException if the oracle is not found (404)
     */
    @GetMapping("/{name}")
    public Oracle getOracle(@PathVariable String name) {
        return Optional.ofNullable(oracleRepository.findByName(name))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Oracle not found: " + name
                ));
    }

    /**
     * Gets a random entry from the specified oracle.
     * @throws ResponseStatusException if the oracle is not found (404)
     */
    @GetMapping("/{name}/random")
    public String getRandomEntry(@PathVariable String name) {
        return Optional.ofNullable(oracleRepository.findByName(name))
                .map(Oracle::randomResult)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oracle not found: " + name));
    }


}
