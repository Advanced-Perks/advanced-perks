package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import de.fabilucius.advancedperks.utilities.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CancelInventoryInteraction
public class PerkToggleGuiElement extends GuiElement {

    private static final ItemStack ACTIVATED_ICON = new ItemStackBuilder(XMaterial.valueOf("LIME_DYE").parseItem())
            .setDisplayName(MessageConfigReceiver.getMessage("Gui.Perk-Toggle.Activated"))
            .build();
    private static final ItemStack DEACTIVATED_ICON = new ItemStackBuilder(XMaterial.valueOf("GRAY_DYE").parseItem())
            .setDisplayName(MessageConfigReceiver.getMessage("Gui.Perk-Toggle.Deactivated"))
            .build();

    private final Perk perk;

    public PerkToggleGuiElement(GuiWindow guiWindow, Perk perk) {
        super(guiWindow, (guiElement, event) -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = ((Player) event.getWhoClicked()).getPlayer();
                AdvancedPerks.getInstance().getPerkStateController().togglePerk(player, perk);
                guiElement.setItemStack(guiElement.getItemStack());
            }
        }, ACTIVATED_ICON);
        this.perk = perk;
        this.refreshIcon();
    }

    public void refreshIcon() {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(this.getGuiWindow().getPlayer());
        ItemStack state = perkData.isPerkActivated(this.getPerk()) ? ACTIVATED_ICON : DEACTIVATED_ICON;
        if (!this.getItemStack().getType().equals(state.getType())) {
            this.setItemStack(state);
        }
    }

    /* the getter and setter of this class */

    @Override
    public ItemStack getItemStack() {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(this.getGuiWindow().getPlayer());
        return perkData.isPerkActivated(this.getPerk()) ? ACTIVATED_ICON : DEACTIVATED_ICON;
    }

    public Perk getPerk() {
        return perk;
    }
}
