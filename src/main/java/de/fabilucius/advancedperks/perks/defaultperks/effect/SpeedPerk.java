package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPerk extends AbstractEffectPerk {
    public SpeedPerk() {
        super("Speed", new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return new ItemStackBuilder(Material.SUGAR)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
