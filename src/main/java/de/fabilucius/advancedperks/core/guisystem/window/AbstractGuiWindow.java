package de.fabilucius.advancedperks.core.guisystem.window;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.persistantdata.UuidPersistentDataType;
import de.fabilucius.advancedperks.external.InventoryUpdate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;

//TODO offload stuff into a proper Interface "GuiWindow" (same for "GuiElement")
public abstract class AbstractGuiWindow implements GuiWindow {

    @Inject
    private Injector injector;

    @Inject
    @Named("uuidKey")
    private NamespacedKey uuidKey;

    private final Map<UUID, GuiElement> guiElements = Maps.newHashMap();
    private final Inventory inventory;
    private final Player player;

    public AbstractGuiWindow(Inventory inventory, Player player) {
        this.inventory = inventory;
        this.player = player;
    }

    @Override
    public void addGuiElement(GuiElement guiElement, int slot) {
        this.injector.injectMembers(guiElement);
        this.guiElements.put(guiElement.getUniqueId(), guiElement);
        this.inventory.setItem(slot, guiElement.getIcon());
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
    public Optional<GuiElement> getGuiElementByItemStack(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.guiElements.get(itemMeta.getPersistentDataContainer().get(this.uuidKey, new UuidPersistentDataType())));
    }

    @Override
    public int getSlot(GuiElement guiElement) {
        OptionalInt index = IntStream.range(0, this.getInventory().getSize())
                .filter(i -> {
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
                })
                .findFirst();
        return index.orElse(-1);
    }

    @Override
    public NamespacedKey getUuidKey() {
        return uuidKey;
    }

    @Override
    public void setTitle(String title) {
        this.getInventory().getViewers().forEach(humanEntity -> {
            if (humanEntity instanceof Player player) {
                InventoryUpdate.updateInventory(player, title);
            }
        });
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
