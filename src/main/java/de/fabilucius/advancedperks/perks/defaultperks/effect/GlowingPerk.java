package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import de.fabilucius.sympel.multiversion.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowingPerk extends AbstractEffectPerk {
    public GlowingPerk() {
        super("Glowing", new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
        this.setMinimumServerVersion(ServerVersion.v1_9);
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.GLOWSTONE_DUST)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
