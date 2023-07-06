package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FrogPerk extends AbstractEffectPerk {
    public FrogPerk() {
        super("Frog", new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.WATER_BUCKET)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
