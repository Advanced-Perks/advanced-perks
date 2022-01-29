package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireResistancePerk extends AbstractEffectPerk {
    public FireResistancePerk() {
        super("FireResistance", new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.LAVA_BUCKET)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
