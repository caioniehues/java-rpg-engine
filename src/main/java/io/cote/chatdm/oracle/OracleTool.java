package io.cote.chatdm.oracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OracleTool {

    private static final Logger logger = LoggerFactory.getLogger(OracleTool.class);

    private final OracleService oracleService;

    public OracleTool(OracleService oracleService) {
        this.oracleService = oracleService;
    }

    private static final String ORACLE_DESCRIPTION = "The name of the oracle to use. If not specified, the default oracle will be used.";

    @Tool(description = "Call a named Oracle which will return a JSON response with the name of the oracle, a description of how to use it, and the result of the oracle. Use the result as your inspiration for what happens next, how to describe something, etc.")
    public String oracle(@ToolParam(description = ORACLE_DESCRIPTION) String oracleName) throws JsonProcessingException {
        if (oracleService.hasOracle(oracleName)) {
            Oracle oracle = oracleService.getOracle(oracleName);
            Map<String, Object> jsonMap = Map.of("name", oracle.getName(), "description", oracle.getDescription(), "result", oracle.getRandomResult());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
            return jsonString;
        } else {
            return "no oracles configured";
        }
    }

    @Tool(description = "Get a list of Oracle available, listing the name of the Oracle and how they could be used.")
    public String listOracles() {
        Map<String,String> oracles = new HashMap<>();
        oracleService.getOracleNames().forEach(name -> oracles.put(name, oracleService.getOracle(name).getDescription()));
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oracles);
            return jsonString;
        } catch (JsonProcessingException e) {
            logger.error("Unable to serialize oracles to JSON", e);
            return "No oracles found. (Something is wrong with the files.)";
    }
}

}
