package io.cote.chatdm.dice;

import com.bernardomg.tabletop.dice.history.DefaultRollHistory;
import com.bernardomg.tabletop.dice.history.RollHistory;
import com.bernardomg.tabletop.dice.interpreter.DiceRoller;
import com.bernardomg.tabletop.dice.parser.DefaultDiceParser;
import com.bernardomg.tabletop.dice.parser.DiceParser;
import io.cote.chatdm.oracle.OracleTool;
import io.cote.chatdm.utils.ChatDMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class DiceRollerTool {

    private static final Logger logger = LoggerFactory.getLogger(DiceRollerTool.class);

    @Tool(name = "ChatDM_rollDice", description = "Rolls dice according to the syntax defined in the D&D 5e rules. Returns total of all dice and also the value of each dice rolled in JSON.")
    public String roll(@ToolParam(description = "Notation for dice to roll such a d6, 2d4+4, 3d6, d20-3, 1d20+7 etc.") String diceExpression,
                        @ToolParam(description = ChatDMUtils.CONTEXT_PARAM, required=true) String context)
    {
        DiceParser diceParser = new DefaultDiceParser();
        RollHistory rolls = new DefaultDiceParser().parse(diceExpression, new DiceRoller());

        // TK we miss out on tracking natural 20s or 1's.

        /*
        TK I want to return a structure like this, which would be easy
        enough to build up.

        {
        "total": 5,
        "originalnotation": "2d4 + 4",
        "dice rolled" :
            {
            "1d4": 3,
            "1d4": 2
            }
        }
         */

        logger.info("Dice {} rolled resulting in {} total, dice rolled {}\nContext: {}", diceExpression, rolls.getTotalRoll(), rolls.getRollResults(), context);
        return rolls.getTotalRoll().toString();
    }

}
