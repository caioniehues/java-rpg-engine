package io.cote.chatdm.dnd.combat;


import io.cote.chatdm.oracle.OracleTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class CombatCheckListTool {
    private static final Logger logger = LoggerFactory.getLogger(CombatCheckListTool.class);

    @Tool(name = "ChatDM_combatCheckList_Prep",
            description = """
                          Whenever you start combat, call this tool to come up with a pre-combat check list to make a plan.
                          During each round of combat, call the ChatDM_combatRoundCheckList and follow the instructions.
                          When it is a monster or NPCs turn, call the ChatDM_combatMonsterActionsCheckList and follow the instructions.
                          At the end of combat, call the ChatDM_combatAfterCheckList and follow the instructions.
                    """)
    public String ChatDM_combatCheckListTool() {
        return """
                Monster goals: What does each creature want in this encounter? Survival, loot, territory, revenge, delay, protect?
                - Behavior plan: Write down behavioral tendencies (e.g., ambush, fight dirty, protect the leader, flee if outmatched).
                - Environment notes: Identify at least two elements (cover, hazards, elevation) that could influence player tactics.
                - Escape routes: Note potential retreat paths for monsters.
                - Treasure / clues: Define what they carry or might drop.
                """;
    }
}
