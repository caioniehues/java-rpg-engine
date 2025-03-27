package io.cote.chatdm.dc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A DC level for an attribute. Based on <a href="https://docs.google.com/spreadsheets/u/0/d/1L0zmuGFMgrlmKHjpqhRv7kA22D5C5LSatwGQZrNtKjM/htmlview?pli=1#">the Monty Cook system</a>.
 *
 * @param attribute   the attribute this applies to like INT or Intelligence.
 * @param description one of {@link DifficultyLevelDescription}
 * @param guidance    description of the difficulty level.
 * @param skill       the skill being checked, if any
 * @param dc
 */
record DifficultyClass(Attribute attribute,
                       DifficultyLevelDescription description,
                       String skill,
                       String guidance,
                       int dc) {

    DifficultyClass {
        Objects.requireNonNull(attribute, "Attribute cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(guidance, "Guidance cannot be null");
        if (dc < 0) {
            throw new IllegalArgumentException("DC must be greater than or equal to zero.");
        }
    }

    /**
     * Do a DC check. If <code>roll</code> is equal to or greater than the DC, it is considered a success. If it is
     * less, a failure
     *
     * @param roll the roll to check.
     * @return true for success, false for failure.
     */
    boolean check(int roll) {
        return roll >= dc;
    }

    /**
     * Enum for difficulty levels.
     */
    enum DifficultyLevelDescription {
        ROUTINE,
        SIMPLE,
        STANDARD,
        DEMANDING,
        DIFFICULT,
        CHALLENGING,
        INTIMIDATING,
        FORMIDABLE,
        HEROIC,
        LEGENDARY,
        IMPOSSIBLE;
    }

    enum Attribute {
        STR,
        CON,
        DEX,
        INT,
        WIS,
        CHA,
        GENERAL;

        static DifficultyClass.Attribute cannonicalizeAttribute(String attribute) {
            return ATTRIBUTE_MAP.get(attribute.toLowerCase());
        }

        private static final Map<String, DifficultyClass.Attribute> ATTRIBUTE_MAP = new HashMap<>() {{
            put("intelligence", DifficultyClass.Attribute.INT);
            put("int", DifficultyClass.Attribute.INT);
            put("strength", DifficultyClass.Attribute.STR);
            put("str", DifficultyClass.Attribute.STR);
            put("dexterity", DifficultyClass.Attribute.DEX);
            put("dex", DifficultyClass.Attribute.DEX);
            put("wisdom", DifficultyClass.Attribute.WIS);
            put("wis", DifficultyClass.Attribute.WIS);
            put("constitution", DifficultyClass.Attribute.CON);
            put("con", DifficultyClass.Attribute.CON);
            put("charisma", DifficultyClass.Attribute.CHA);
            put("cha", DifficultyClass.Attribute.CHA);
            // if it isn't one of the above.
            put("general", DifficultyClass.Attribute.GENERAL);
            put("gen", DifficultyClass.Attribute.GENERAL);
            put("", DifficultyClass.Attribute.GENERAL);
            put(null, DifficultyClass.Attribute.GENERAL);
        }};
    }

    enum Skill {
        ACROBATICS,
        ANIMAL_HANDLING,
        ARCANA,
        ATHLETICS,
        DECEPTION,
        HISTORY,
        INSIGHT,
        INTIMIDATION,
        INVESTIGATION,
    }
}
