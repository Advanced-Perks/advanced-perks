package de.fabilucius.advancedperks.perk;

import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Perk {

    String getIdentifier();

    String getDisplayName();

    PerkDescription getDescription();

    Set<String> getDisallowedWorlds();

    PerkGuiIcon getPerkGuiIcon();

    boolean isEnabled();

    Map<String, Object> getFlags();

    Optional<String> getPermission();

    Optional<Double> getPrice();

}
