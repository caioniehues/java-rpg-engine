package io.cote.chatdm.oracle;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/chatdm/api/oracle")
public class OracleController {

    private final OracleService oracleService;

    public OracleController(OracleService oracleService) {
        this.oracleService = oracleService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Here is the Oracle controller.";
    }

    /**
     * Gets all oracle names.
     */
    @GetMapping
    public ResponseEntity<Set<String>> getAllOracleNames() {
        return ResponseEntity.ok(oracleService.getOracleNames());
    }

    /**
     * Gets detailed information about all oracles.
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Integer>> getOracleInfo() {

        return ResponseEntity.ok(oracleService.getOracleInfo());
    }

    /**
     * Gets a random entry from the specified oracle.
     */
    @GetMapping("/{name}/random")
    public ResponseEntity<?> getRandomEntry(@PathVariable String name) {
        Oracle oracle = oracleService.getOracle(name);
        if (!oracleService.hasOracle(name)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Oracle not found: " + name);
            return ResponseEntity.notFound().build();
        }
        else {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("oracle", oracle.getName());
            response.put("entry", oracle.getRandomEntry());
            return ResponseEntity.ok(response);
        }
    }

}
