package de.fabilucius.advancedperks.perk.defaultperks.effect;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.EffectPerk;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

@PerkIdentifier("poseidon")
public class PoseidonPerk extends AbstractDefaultPerk implements EffectPerk {
    public PoseidonPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return Lists.newArrayList(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
    }
}
