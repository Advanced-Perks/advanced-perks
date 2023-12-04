package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.data.state.PerkToggleResult;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkToggleElement extends AbstractGuiElement {

    @Inject
    private PerkStateController perkStateController;

    @Inject
    private AdvancedPerks advancedPerks;

    private final MessagesConfiguration messagesConfiguration;
    private final Perk perk;
    private final String enabledText;
    private final String disabledText;

    //TODO currently unneeded tidy this mess up a bit
    public PerkToggleElement(GuiWindow guiWindow, MessagesConfiguration messagesConfiguration, Perk perk, boolean enabled, boolean unlocked, String notUnlockedText, String enabledText, String disabledText) {
        super(guiWindow, ItemStackBuilder.fromMaterial(!unlocked ? Material.IRON_BARS : enabled ? Material.LIME_DYE : Material.GRAY_DYE)
                .setDisplayName(!unlocked ? notUnlockedText : enabled ? enabledText : disabledText)
                .build());
        this.messagesConfiguration = messagesConfiguration;
        this.perk = perk;
        this.enabledText = enabledText;
        this.disabledText = disabledText;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            PerkToggleResult result = this.perkStateController.togglePerk(this.getGuiWindow().getPlayer(), this.perk);
            switch (result) {
                case EVENT_CANCELLED, DISALLOWED_WORLD, NO_PERMISSION, TOO_MANY_ACTIVE -> {
                    this.getGuiWindow().setTitle(this.messagesConfiguration.getComputedString("gui.perk_gui.toggle." + result.name().toLowerCase()));
                    Bukkit.getScheduler().runTaskLater(this.advancedPerks, () -> this.getGuiWindow().setTitle(this.messagesConfiguration.getComputedString("gui.perk_gui.title")), 20L);
                    this.playSound(GuiSound.ERROR_CLICK);
                }
                case ENABLED -> {
                    this.setIconAndUpdate(ItemStackBuilder.fromMaterial(Material.LIME_DYE)
                            .setDisplayName(this.enabledText)
                            .build());
                    this.playSound(GuiSound.ON_CLICK);
                }
                case DISABLED -> {
                    this.setIconAndUpdate(ItemStackBuilder.fromMaterial(Material.GRAY_DYE)
                            .setDisplayName(this.disabledText)
                            .build());
                    this.playSound(GuiSound.OFF_CLICK);
                }
            }
        };
    }
}
