package de.fabilucius.advancedperks.core.guisystem.window;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.persistantdata.UuidPersistentDataType;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;

public abstract class AbstractGuiWindow implements GuiWindow {

    private static final boolean SET_TITLE_SUPPORT;

    static {
        boolean support = false;
        try {
            InventoryView.class.getMethod("setTitle", String.class);
            support = true;
        } catch (NoSuchMethodException ignored) {
        } finally {
            SET_TITLE_SUPPORT = support;
        }
    }

    @Inject
    private Injector injector;

    @Inject
    @Named("uuidKey")
    private NamespacedKey uuidKey;

    private final Map<UUID, GuiElement> guiElements = Maps.newHashMap();
    private final Inventory inventory;
    private final Player player;
    private final boolean sounds;

    protected AbstractGuiWindow(Inventory inventory, Player player, boolean sounds) {
        this.inventory = inventory;
        this.player = player;
        this.sounds = sounds;
    }

    @Override
    public void addGuiElement(GuiElement guiElement, int slot) {
        this.injector.injectMembers(guiElement);
        this.guiElements.entrySet().removeIf(uuidGuiElementEntry -> slot == this.getSlot(uuidGuiElementEntry.getValue()));
        this.guiElements.put(guiElement.getUniqueId(), guiElement);
        this.inventory.setItem(slot, guiElement.getIcon());
    }

    @Override
    public void removeGuiElement(GuiElement guiElement) {
        int slot = this.getSlot(guiElement);
        if (slot != -1) {
            this.getInventory().setItem(slot, ItemStackBuilder.fromMaterial(Material.AIR).build());
        }
        this.guiElements.remove(guiElement.getUniqueId());
    }

    @Override
    public Map<UUID, GuiElement> getGuiElements() {
        return guiElements;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void clearGui() {
        this.getGuiElements().clear();
        this.getInventory().clear();
    }

    @Override
    public Optional<GuiElement> getGuiElementByItemStack(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.guiElements.get(itemMeta.getPersistentDataContainer().get(this.uuidKey, new UuidPersistentDataType())));
    }

    @Override
    public int getSlot(GuiElement guiElement) {
        OptionalInt index = IntStream.range(0, this.getInventory().getSize()).filter(i -> {
            ItemStack itemStack = this.getInventory().getItem(i);
            if (itemStack == null) {
                return false;
            } else {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null) {
                    return false;
                }
                UUID uuid = itemMeta.getPersistentDataContainer().get(uuidKey, new UuidPersistentDataType());
                return uuid != null && uuid.equals(guiElement.getUniqueId());
            }
        }).findFirst();
        return index.orElse(-1);
    }

    @Override
    public NamespacedKey getUuidKey() {
        return uuidKey;
    }

    @Override
    public void setTitle(String title) {
        if (!SET_TITLE_SUPPORT) {
            return;
        }
        this.getInventory().getViewers().forEach(humanEntity -> {
            if (humanEntity instanceof Player viewer) {
                viewer.getOpenInventory().setTitle(title);
            }
        });
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void playSound(GuiSound guiSound) {
        if (this.sounds) {
            this.getPlayer().playSound(this.getPlayer().getLocation(), guiSound.getSound(), guiSound.getVolume(), guiSound.getPitch());
        }
    }
}
