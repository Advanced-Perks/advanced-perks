package de.fabilucius.advancedperks.perks.defaultperks.effect;

import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HulkPerk extends AbstractEffectPerk {
    public HulkPerk() {
        super("Hulk", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return new ItemStackBuilder(Material.AIR)
                .setHeadBase64Value("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NkY2MyMzJjYTRiY2M0MjgyZWNjZDdlYzI1ODQ4ODE2ODFhNWZhNGRlZGJkNWU0YmEyYjhiNDdmMzY5ZGUifX19")
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();

    }
}
