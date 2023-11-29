package de.fabilucius.advancedperks.core.guisystem.window.types;

import de.fabilucius.advancedperks.core.guisystem.window.AbstractGuiWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class AbstractPageGuiWindow extends AbstractGuiWindow {

    private int page;

    public AbstractPageGuiWindow(Inventory inventory, Player player) {
        super(inventory, player);
    }

    @Override
    public void initializeGui() {
        this.initializeGui(this.page);
    }

    public abstract void initializeGui(int page);

    public void nextPage() {
        this.page++;
        this.getGuiElements().clear();
        this.getInventory().clear();
        this.initializeGui(this.page);
    }

    public void previousPage() {
        if (this.page == 0) {
            return;
        }
        this.page--;
        this.getGuiElements().clear();
        this.getInventory().clear();
        this.initializeGui(this.page);
    }

    public int getPage() {
        return page;
    }
}
