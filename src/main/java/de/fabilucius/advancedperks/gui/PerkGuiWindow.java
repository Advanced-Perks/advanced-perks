package de.fabilucius.advancedperks.gui;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.gui.elements.*;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.utilities.IterableUtilities;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PerkGuiWindow extends GuiWindow {

    public static final int PERKS_PER_PAGE = 8;

    public PerkGuiWindow(Player player) {
        super(Bukkit.createInventory(null, 54, MessageConfigReceiver.getMessage("Gui.Title")), player);
    }

    @Override
    public void initialize() {
        this.clearGuiWindow();
        List<Perk> perks = AdvancedPerks.getInstance().getPerkRegistry().getPerks();
        int maxPages = (perks.size() / PERKS_PER_PAGE);
        if (this.getPage() != 0) {
            this.addGuiElement(47, new PreviousPageGuiElement(this));
        }
        if (this.getPage() != maxPages && perks.size() % 8 != 0) {
            this.addGuiElement(51, new NextPageGuiElement(this));
        }

        List<Perk> perksFromCurrentPage = perks.subList(this.getPage() * PERKS_PER_PAGE,
                Math.min(perks.size(), (this.getPage() + 1) * PERKS_PER_PAGE));
        List<Integer> perkSlots = Arrays.asList(1, 3, 5, 7, 19, 21, 23, 25);

        IterableUtilities.iterateSimultaneously(perksFromCurrentPage, perkSlots, (perk, integer) -> {
            this.addGuiElement(integer + 9, new PerkToggleGuiElement(this, perk));
            this.addGuiElement(integer, new PerkIconGuiElement(this, perk));
        });
        this.addGuiElement(45, new DisableAllPerksElement(this));
        this.addGuiElement(53, new CloseGuiElement(this));
    }
}
