package de.fabilucius.advancedperks.guisystem.setupperkgui;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.type.PerkGuiConfiguration;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.GuiBackgroundElement;
import de.fabilucius.advancedperks.core.guisystem.window.AbstractGuiWindow;
import de.fabilucius.advancedperks.guisystem.configuration.PerkGuiSaveResult;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.CloseGuiPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.DisableAllPerksPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.NextPagePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PerkIconPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PerkTogglePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PreviousPagePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.SetupGuiPlaceholderElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SetupPerkGuiWindow extends AbstractGuiWindow {

    private final PerkGuiConfiguration perkGuiConfiguration;
    private boolean background;

    @Inject
    public SetupPerkGuiWindow(PerkGuiConfiguration perkGuiConfiguration, Player player, boolean sounds) {
        super(Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "Click on Setup Icon to save."), player, sounds);
        this.perkGuiConfiguration = perkGuiConfiguration;
        this.background = perkGuiConfiguration.hasBackground();
    }

    @Override
    public void initializeGui() {
        if (this.hasBackground()) {
            for (int i = 0; i < this.getInventory().getSize(); i++) {
                this.addGuiElement(new GuiBackgroundElement(this), i);
            }
        }
        AtomicInteger index = new AtomicInteger();
        this.perkGuiConfiguration.getPerkIconLocations().forEach(perkIconLocation -> {
            this.addGuiElement(new PerkIconPlaceholderElement(this, index.get()), perkIconLocation.iconSlot());
            this.addGuiElement(new PerkTogglePlaceholderElement(this, index.get()), perkIconLocation.toggleSlot());
            index.incrementAndGet();
        });
        this.addGuiElement(new PreviousPagePlaceholderElement(this), this.perkGuiConfiguration.getPreviousPageSlot());
        this.addGuiElement(new NextPagePlaceholderElement(this), this.perkGuiConfiguration.getNextPageSlot());
        this.addGuiElement(new CloseGuiPlaceholderElement(this), this.perkGuiConfiguration.getCloseGuiSlot());
        this.addGuiElement(new DisableAllPerksPlaceholderElement(this), this.perkGuiConfiguration.getDisableAllPerksSlot());
        this.addGuiElement(new SetupGuiPlaceholderElement(this), this.perkGuiConfiguration.getSetupGuiSlot());
    }

    public PerkGuiSaveResult save() {
        //TODO save gui
        return PerkGuiSaveResult.ERROR;
    }

    public boolean hasBackground() {
        return background;
    }

    public void toggleHasBackground() {
        this.background = !this.background;
        if (this.background) {
            for (int i = 0; i < this.getInventory().getSize(); i++) {
                int finalI = i;
                if (this.getGuiElements().values().stream().noneMatch(guiElement -> this.getSlot(guiElement) == finalI)) {
                    this.addGuiElement(new GuiBackgroundElement(this), i);
                }
            }
        } else {
            List<GuiElement> toRemove = this.getGuiElements().values().stream()
                    .filter(GuiBackgroundElement.class::isInstance)
                    .toList();
            toRemove.forEach(this::removeGuiElement);
        }
    }


}
