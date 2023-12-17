package de.fabilucius.advancedperks.testdata.perk;

import de.fabilucius.advancedperks.perk.AbstractPerk;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;

import java.util.Map;

public abstract class AbstractTestPerk extends AbstractPerk {
    protected AbstractTestPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }
}
