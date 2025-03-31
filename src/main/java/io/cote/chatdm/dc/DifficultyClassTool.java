package io.cote.chatdm.dc;

import io.cote.chatdm.dice.DiceRollerTool;
import io.cote.chatdm.dmstore.FileDMStoreRepository;
import io.cote.chatdm.utils.ChatDMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.io.IOException;

@Service
public class DifficultyClassTool {

    private static final Logger logger = LoggerFactory.getLogger(DifficultyClassTool.class);

    private final FileDMStoreRepository dmStoreRepository;

    public DifficultyClassTool(FileDMStoreRepository dmStoreRepository) {
        this.dmStoreRepository = dmStoreRepository;
    }

    @Tool(name = "ChatDM_difficultyClassLookup",
            description = "Looks up the table of suggested difficulty classes (DC) checks for skill checks in D&D 5e. A check may be a specific skill (like stealth, athletics, arcana), a type of activity, or just a general attribute check (Strength, Dexterity, Charisma, etc.). You will be given back a table of suggestions and guidance for what the DC for various tasks would be. The table returned will be JSON.")
    public String dcLookup(@ToolParam(description = "The attribute, skill, or challenge that the difficulty class (DC) check is for: this is one of Strength, Constitution, Dexterity, Intelligence, Wisdom, or Charisma. If an attribute one is not specified or the parameter is unclear, a general table for DCs will be returned.", required = true) String attributeOrSkill,
                           @ToolParam(description = ChatDMUtils.CONTEXT_PARAM, required = true) String context)
            throws IOException {
        logger.info("Difficulty class lookup for {} context {}", context, context);

        // Make sure we have the attribute name we can work with.
        DifficultyClass.Attribute canonicalAttribute = DifficultyClass.Attribute.cannonicalizeAttribute(attributeOrSkill);
        String dcTable = dmStoreRepository.loadToString("dc/GENERAL_difficulty_class.json.st");
        // I haven't implemented the more fancy way of doing this.
        //String dcTable = dmStoreRepository.loadToString(String.format("dc/%s_difficulty_class_table.st", canonicalAttribute.name()));
//        if (dcTable == null) {
//            // Double check that there isn't a GENERAL table.
//            dcTable = dmStoreRepository.loadToString(String.format("dc/%s_difficulty_class_table.st", DifficultyClass.Attribute.GENERAL.name()));
//            if (dcTable == null) {
//                logger.debug(String.format("No table found for attribute %s, and %s table missing.", attributeOrSkill, DifficultyClass.Attribute.GENERAL.name()));
//                return String.format("You must come up with your own DC.");
//            }
        //}

        if (dcTable == null) {
            logger.debug(String.format("No table found for attribute %s, and %s table missing.", attributeOrSkill, DifficultyClass.Attribute.GENERAL.name()));
            return String.format("You must come up with your own DC.");
        }
        else { return new ST(dcTable).render(); }

    }


}
