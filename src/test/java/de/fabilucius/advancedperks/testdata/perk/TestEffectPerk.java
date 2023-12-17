package de.fabilucius.advancedperks.testdata.perk;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.EffectPerk;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class TestEffectPerk extends AbstractTestPerk implements EffectPerk {

    private final List<PotionEffect> potionEffects;

    public TestEffectPerk(List<PotionEffect> potionEffects) {
        super(
                "test_effect_perk",
                "Test Effect Perk",
                new PerkDescription(Lists.newArrayList("Test Line 1", "Test Line 2")),
                new PerkGuiIcon(Material.DIRT),
                true,
                Maps.newHashMap()
        );
        this.potionEffects = potionEffects;
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }
}
