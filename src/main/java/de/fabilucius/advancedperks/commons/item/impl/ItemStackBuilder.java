package de.fabilucius.advancedperks.commons.item.impl;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.commons.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;

public class ItemStackBuilder implements ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    protected ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.getItemStack().getItemMeta();
    }

    public static ItemStackBuilder fromMaterial(Material material) {
        Preconditions.checkNotNull(material, "material cannot be null");
        return new ItemStackBuilder(new ItemStack(material));
    }

    public static ItemStackBuilder fromItemStack(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");
        return new ItemStackBuilder(itemStack);
    }

    /**
     * This method will try to find a material based on the provided string with the use of the XMaterial library, a library that
     * aims to provide multi-version material support.
     * If no material was found with the input but the material exists on some spigot version XMaterial is either outdated or missing the
     * item.
     * For either case please contact me and for the first consider contacting the maintainer of XMaterial as well.
     *
     * @param approximateMaterial the material to hopefully find :)
     * @return the itemStack if an appropriate material was found
     */
    public static ItemStackBuilder fromApproximateMaterial(String approximateMaterial) {
        Preconditions.checkNotNull(approximateMaterial, "approximateMaterial cannot be null");
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(approximateMaterial);
        Preconditions.checkState(xMaterial.isPresent(), approximateMaterial + " cannot be mapped to a potential material");
        ItemStack itemStack = xMaterial.get().parseItem();
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");
        return new ItemStackBuilder(itemStack);
    }

    public ItemStackBuilder setDisplayName(String displayName) {
        Preconditions.checkNotNull(displayName, "displayName cannot be null");
        this.getItemMeta().setDisplayName(displayName);
        return this;
    }

    public ItemStackBuilder setDescription(List<String> description) {
        Preconditions.checkNotNull(description, "description cannot be null");
        this.getItemMeta().setLore(description);
        return this;
    }

    public ItemStackBuilder setDescription(String... description) {
        Preconditions.checkNotNull(description, "description cannot be null");
        return this.setDescription(Lists.newArrayList(description));
    }

    public ItemStackBuilder setDescription(String description) {
        Preconditions.checkNotNull(description, "description cannot be null");
        return this.setDescription(Lists.newArrayList(description));
    }

    public ItemStackBuilder setAmount(int amount) {
        Preconditions.checkState(amount > 0, "amount cannot be smaller than 0");
        this.getItemStack().setAmount(amount);
        return this;
    }

    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        this.getItemMeta().setUnbreakable(unbreakable);
        return this;
    }

    public <T, Z> ItemStackBuilder addPersistentData(NamespacedKey key, PersistentDataType<T, Z> persistentDataType, Z value) {
        this.getItemMeta().getPersistentDataContainer().set(key, persistentDataType, value);
        return this;
    }

    @Override
    public ItemStack build() {
        this.getItemStack().setItemMeta(this.getItemMeta());
        return this.getItemStack();
    }

    /* the getter and setter of the class */

    @Override
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
