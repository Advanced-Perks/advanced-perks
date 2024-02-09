package de.fabilucius.advancedperks.core.itembuilder.types.skull;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is exactly like the {@link ItemStackBuilder} except it enforces to modify a skull item.
 * It also contains some additional methods to modify the skull item itself.
 * This makes it able to change the skull owner, owning player or the texture based on a base64 string.
 */
public class SkullStackBuilder extends ItemStackBuilder {

    private static final Logger LOGGER = Bukkit.getLogger();

    protected SkullStackBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public static SkullStackBuilder fromMaterial(Material material) {
        Preconditions.checkNotNull(material, "material cannot be null");
        Preconditions.checkState(material.name().contains("SKULL") ||
                material.name().contains("HEAD"), "material needs to be a skull");
        return new SkullStackBuilder(new ItemStack(material));
    }

    public static SkullStackBuilder fromItemStack(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");
        Material material = itemStack.getType();
        Preconditions.checkState(material.name().contains("SKULL") ||
                material.name().contains("HEAD"), "material needs to be a skull");
        return new SkullStackBuilder(itemStack);
    }

    /**
     * This method will try to find a material based on the provided string with the use of the XMaterial library, a library that
     * aims to provide multi-version material support.
     * If no material was found with the input but the material exists on some spigot version XMaterial is either outdated or missing the
     * item.
     * For either case please contact me and for the first consider contacting the maintainer of XMaterial as well.
     * As a bonus this method will ensure that the material wanted is a skull.
     *
     * @param approximateMaterial the material to hopefully find :)
     * @return the itemStack if an appropriate material was found
     */
    public static SkullStackBuilder fromApproximateMaterial(String approximateMaterial) {
        Preconditions.checkNotNull(approximateMaterial, "approximateMaterial cannot be null");
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(approximateMaterial);
        Preconditions.checkState(xMaterial.isPresent(), "%s cannot be mapped to a potential material".formatted(approximateMaterial));
        ItemStack itemStack = xMaterial.get().parseItem();
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");
        Material material = itemStack.getType();
        Preconditions.checkState(material.name().contains("SKULL") ||
                material.name().contains("HEAD"), "material needs to be a skull");
        return new SkullStackBuilder(itemStack);
    }

    public SkullStackBuilder setBase64Value(String base64Value) {
        try {
            try {
                Class.forName("org.bukkit.profile.PlayerProfile");
                return LatestSkullStackBuilder.setBase64Value(this, base64Value);
            } catch (ClassNotFoundException e) {
                Field field = this.getItemMeta().getClass().getDeclaredField("profile");
                field.setAccessible(true);
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "base64head");
                gameProfile.getProperties().put("textures", new Property("textures", base64Value));
                field.set(this.getItemMeta(), gameProfile);
            } catch (MalformedURLException e) {
                LOGGER.log(Level.WARNING, "The ItemStackBuilder was unable to set the base64 value:", e);
            }
        } catch (NoSuchFieldException event) {
            LOGGER.log(Level.WARNING, "The ItemStackBuilder was unable to find the profile field this should " +
                    "under normal circumstances never happen, maybe you use a heavily customized spigot version.");
        } catch (IllegalAccessException event) {
            LOGGER.log(Level.WARNING, "The ItemStackBuilder was unable to set the profile with the modified game profile:", event);
        }
        return this;
    }

    public SkullStackBuilder setOwningPlayer(OfflinePlayer offlinePlayer) {
        Preconditions.checkNotNull(offlinePlayer, "offlinePlayer cannot be null");
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        return this;
    }

    @Deprecated
    public SkullStackBuilder setOwner(String owner) {
        Preconditions.checkNotNull(owner, "owner cannot be null");
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwner(owner);
        return this;
    }

}