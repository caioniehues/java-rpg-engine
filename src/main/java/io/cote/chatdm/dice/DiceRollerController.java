package io.cote.chatdm.dice;

import com.bernardomg.tabletop.dice.interpreter.DiceRoller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatdm/api/diceroll")
class DiceRollerController {

    @GetMapping("/{dice}")
    String rollDice(@PathVariable String dice) {
        return new DiceRollerTool().roll(dice, "");
    }
}
