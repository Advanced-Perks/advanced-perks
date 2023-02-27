package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoHungerPerk extends AbstractEffectPerk {
    public NoHungerPerk() {
        super("NoHunger", new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.COOKED_BEEF)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
