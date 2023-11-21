package de.fabilucius.advancedperks.perk;

import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Perk {

    String getIdentifier();

    String getDisplayName();

    PerkDescription getDescription();

    PerkGuiIcon getPerkGuiIcon();

    boolean isEnabled();

    Map<String, Object> getFlags();

    Optional<String> getPermission();

    Optional<Double> getPrice();

    Optional<List<String>> getDisallowedWorlds();

}
