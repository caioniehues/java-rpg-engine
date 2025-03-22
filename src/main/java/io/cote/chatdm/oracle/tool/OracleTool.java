package io.cote.chatdm.oracle.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cote.chatdm.oracle.Oracle;
import io.cote.chatdm.oracle.repository.OracleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * A generic tool for an Oracle, a random table of words, events, etc. Oracles are filled with the content, name, and
 * description of yaml files.
 */
@Service
public class OracleTool {

    private static final Logger logger = LoggerFactory.getLogger(OracleTool.class);
    private final OracleRepository oracleRepository;

    public OracleTool(OracleRepository oracleRepository) {
        this.oracleRepository = oracleRepository;
    }

    @Tool(description = "Call a named Oracle which will return a JSON response with the name of the oracle, a description of how to use it, and the result of the oracle. Use the result as your inspiration for what happens next, how to describe something, etc.")
    public String chatDM_oracle(@ToolParam(description = "Name of oracle to be used.") String oracleName,
                         @ToolParam(description = Utils.CONTEXT_PARAM, required = false) String context) throws JsonProcessingException {
        if (oracleRepository.existsByName(oracleName)) {
            Oracle oracle = oracleRepository.findByName(oracleName);
            Map<String, Object> jsonMap = Map.of("name", oracle.name(), "description", oracle.description(), "result", Oracle.randomResult(oracle));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        } else {
            return String.format("No oracle with name %s found.", oracleName);
        }
    }

    @Tool(description = """
            When playing a role playing game, like D&D, it is useful to have Oracles to randomly determine what happens and come up with ideas. This tool gets a list of the oracle available, listing the name of the Oracle and how they could be used. Oracles in solo D&D serve as randomized decision-making tools that replace a human Dungeon Master, allowing lone players to experience unpredictable gameplay. They generate impartial responses to player questions, create emergent storytelling by introducing unexpected elements, fill in world details like NPC motivations or location features, make objective rulings on action success, and maintain game balance through complications or twists. Ranging from simple yes/no probability tools to complex tables and random event generators, oracles provide the genuine surprise and challenge typically supplied by another person. By consulting these systems at key decision points, solo players can avoid predetermined outcomes and experience a dynamic narrative that unfolds organically rather than following a scripted path they'd consciously or unconsciously create themselves.
            """)
    public String chatDM_listOracles() {
        Map<String, String> oracles = new HashMap<>();
        oracleRepository.findAllNames().forEach(name -> oracles.put(name, oracleRepository.findByName(name).description()));
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oracles);
        } catch (JsonProcessingException e) {
            logger.error("Unable to serialize oracles to JSON", e);
            return "No oracles found. (Something is wrong with the files.)";
        }
    }

}
