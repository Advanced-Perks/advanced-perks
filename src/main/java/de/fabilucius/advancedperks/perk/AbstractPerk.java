package de.fabilucius.advancedperks.perk;

import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.EffectPerk;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractPerk implements Perk {

    private final String identifier;
    private final String displayName;
    private final PerkDescription perkDescription;
    private final PerkGuiIcon perkGuiIcon;
    private final boolean enabled;
    private final Map<String, Object> flags;

    public AbstractPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.perkDescription = perkDescription;
        this.perkGuiIcon = perkGuiIcon;
        this.enabled = enabled;
        this.flags = flags;
    }

    @Override
    public final void onPrePerkEnable(Player player) {
        if (this instanceof EffectPerk effectPerk) {
            player.addPotionEffects(effectPerk.getPotionEffects());
        }
        this.onPerkEnable(player);
    }

    @Override
    public void onPrePerkDisable(Player player) {
        if (this instanceof EffectPerk effectPerk) {
            effectPerk.getPotionEffects().forEach(potionEffect ->
                    player.removePotionEffect(potionEffect.getType()));
        }
        this.onPerkDisable(player);
    }

    @Override
    public void onPerkEnable(Player player) {
    }

    @Override
    public void onPerkDisable(Player player) {
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public PerkDescription getDescription() {
        return perkDescription;
    }

    @Override
    public PerkGuiIcon getPerkGuiIcon() {
        return perkGuiIcon;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Map<String, Object> getFlags() {
        return flags;
    }

    /* Flag based properties */

    @Override
    public Optional<String> getPermission() {
        try {
            return Optional.ofNullable(this.flags.get("permission").toString());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> getPrice() {
        try {
            return Optional.of(Double.valueOf(String.valueOf(this.flags.get("price"))));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<List<String>> getDisallowedWorlds() {
        try {
            List<String> disallowedWorlds = (List<String>) this.flags.get("disallowed_worlds");
            return disallowedWorlds.isEmpty() ? Optional.empty() : Optional.of(disallowedWorlds);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "AbstractPerk{" +
                "identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", perkDescription=" + perkDescription +
                ", perkGuiIcon=" + perkGuiIcon +
                ", enabled=" + enabled +
                ", flags=" + flags +
                '}';
    }
}
