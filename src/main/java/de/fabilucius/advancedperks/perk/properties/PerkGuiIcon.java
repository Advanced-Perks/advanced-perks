package de.fabilucius.advancedperks.perk.properties;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Strings;
import de.fabilucius.advancedperks.core.itembuilder.types.skull.SkullStackBuilder;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PerkGuiIcon {

    private static final String MISSING_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdjOTE0MTE1ODczYzQxZTM2NjliNjllZDIwMmE2ZjAyNjU3Zjc2ZGUyZGY2MjRlYWU4NDVjNDUyZGU1NWVjMiJ9fX0=";
    private final ItemStack itemStack;

    public PerkGuiIcon(String data) {
        if (Strings.isNullOrEmpty(data)) {
            this.itemStack = SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
                    .setBase64Value(MISSING_TEXTURE)
                    .build();
        } else {
            Optional<XMaterial> approximateMaterial = XMaterial.matchXMaterial(data);
            if (approximateMaterial.isPresent()) {
                this.itemStack = approximateMaterial.get().parseItem();
            } else if (Base64.isBase64(data)) {
                this.itemStack = SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
                        .setBase64Value(data)
                        .build();
            } else {
                throw new IllegalArgumentException("Unable to parse the perk gui icon the following data is neither a valid material nor a valid base64 encoded skull texture: %s".formatted(data));
            }
        }
    }

    public PerkGuiIcon(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemStack getIcon() {
        return this.itemStack;
    }

}
