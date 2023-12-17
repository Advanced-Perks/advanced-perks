package de.fabilucius.advancedperks.perk;

import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Perk {

    void onPerkEnable(Player player);

    void onPrePerkEnable(Player player);

    void onPerkDisable(Player player);

    void onPrePerkDisable(Player player);

    String getIdentifier();

    String getDisplayName();

    PerkDescription getDescription();

    PerkGuiIcon getPerkGuiIcon();

    boolean isEnabled();

    Map<String, Object> getFlags();

    Optional<String> getPermission();

    Optional<Double> getPrice();

    Optional<List<String>> getDisallowedWorlds();

    void refreshPerkFlags(Map<String, Object> flags);

}
