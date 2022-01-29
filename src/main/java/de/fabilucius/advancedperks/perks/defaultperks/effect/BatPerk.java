package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BatPerk extends AbstractEffectPerk {
    public BatPerk() {
        super("Bat", new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return new ItemStackBuilder(Material.ENDER_EYE)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
