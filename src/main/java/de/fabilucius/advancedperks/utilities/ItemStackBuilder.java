package de.fabilucius.advancedperks.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fabilucius.advancedperks.commons.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemStackBuilder {
    private static final Logger LOGGER = Bukkit.getLogger();

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemStackBuilder() {
        this(Material.AIR);
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public ItemStackBuilder setDisplayName(String displayName) {
        if (this.getItemMeta() != null) {
            this.getItemMeta().setDisplayName(displayName);
        }
        return this;
    }

    public ItemStackBuilder setDescription(List<String> description) {
        if (this.getItemMeta() != null) {
            this.getItemMeta().setLore(description);
        }
        return this;
    }

    public ItemStackBuilder setHeadBase64Value(String base64Value) {
        this.itemStack = ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_13) ? new ItemStack(Material.getMaterial("PLAYER_HEAD")) :
                new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
        this.itemMeta = this.getItemStack().getItemMeta();
        if (this.getItemMeta() instanceof SkullMeta) {
            try {
                Field field = this.getItemMeta().getClass().getDeclaredField("profile");
                field.setAccessible(true);
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "base64head");
                gameProfile.getProperties().put("textures", new Property("textures", base64Value));
                field.set(this.getItemMeta(), gameProfile);
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "An error occurred while changing the base64 texture value of an itemstack:", exception);
            }
        }
        return this;
    }

    public ItemStack build() {
        if (this.getItemMeta() != null) {
            this.getItemStack().setItemMeta(this.getItemMeta());
        }
        return this.getItemStack();
    }

    /* the getter and setter of this class */

    private ItemStack getItemStack() {
        return itemStack;
    }

    private ItemMeta getItemMeta() {
        return itemMeta;
    }
}
