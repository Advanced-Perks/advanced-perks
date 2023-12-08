package de.fabilucius.advancedperks.guisystem.perkgui;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.CloseGuiWindowElement;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.GuiBackgroundElement;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.NextPageElement;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.PreviousPageElement;
import de.fabilucius.advancedperks.core.guisystem.window.types.AbstractPageGuiWindow;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.data.state.PerkUseStatus;
import de.fabilucius.advancedperks.guisystem.configuration.PerkGuiConfiguration;
import de.fabilucius.advancedperks.guisystem.perkgui.elements.DisableAllPerksElement;
import de.fabilucius.advancedperks.guisystem.perkgui.elements.PerkGuiSetupElement;
import de.fabilucius.advancedperks.guisystem.perkgui.elements.PerkIconElement;
import de.fabilucius.advancedperks.guisystem.perkgui.elements.PerkToggleElement;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerkGuiWindow extends AbstractPageGuiWindow {

    private static final int PERKS_PER_PAGE = 8;

    private final PerkGuiConfiguration perkGuiConfiguration;
    private final MessagesConfiguration messagesConfiguration;

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private PerkStateController perkStateController;

    public PerkGuiWindow(ConfigurationLoader configurationLoader, SettingsConfiguration settingsConfiguration, MessagesConfiguration messagesConfiguration, Player player) throws ConfigurationInitializationException {
        super(Bukkit.createInventory(null, 54, messagesConfiguration.getComputedString("gui.perk_gui.title")), player, settingsConfiguration.isGuiClickSoundsEnabled());
        this.perkGuiConfiguration = configurationLoader.getConfigurationAndLoad(PerkGuiConfiguration.class);
        this.messagesConfiguration = messagesConfiguration;
    }

    @Override
    public void initializeGui(int page) {
        if (this.perkGuiConfiguration.hasBackground()) {
            for (int i = 0; i < this.getInventory().getSize(); i++) {
                this.addGuiElement(new GuiBackgroundElement(this), i);
            }
        }
        List<Perk> perks = this.perkRegistryImpl.getPerks().stream().toList().subList(page * PERKS_PER_PAGE,
                Math.min(perkRegistryImpl.getPerks().size(), (page + 1) * PERKS_PER_PAGE));
        AtomicInteger index = new AtomicInteger();
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(this.getPlayer());
        this.perkGuiConfiguration.getPerkIconLocations().forEach(perkIconLocation -> {
            int currentIndex = index.getAndIncrement();
            if (currentIndex < perks.size()) {
                Perk perk = perks.get(currentIndex);
                if (perk != null) {
                    this.addGuiElement(new PerkIconElement(this, perk), perkIconLocation.iconSlot());
                    this.addGuiElement(new PerkToggleElement(this, this.messagesConfiguration, perk, perkData.getEnabledPerks().contains(perk), !this.perkStateController.canUsePerk(this.getPlayer(), perk).equals(PerkUseStatus.NO_PERMISSION), this.messagesConfiguration.getComputedString("gui.perk_gui.toggle.not_unlocked"), this.messagesConfiguration.getComputedString("gui.perk_gui.toggle.enabled"), this.messagesConfiguration.getComputedString("gui.perk_gui.toggle.disabled")), perkIconLocation.toggleSlot());
                }
            }
        });
        this.addGuiElement(new CloseGuiWindowElement(this, this.messagesConfiguration.getComputedString("gui.perk_gui.close_gui")), this.perkGuiConfiguration.getCloseGuiSlot());
        this.addGuiElement(new DisableAllPerksElement(this, this.messagesConfiguration.getComputedString("gui.perk_gui.disable_all_perks")), this.perkGuiConfiguration.getDisableAllPerksSlot());
        if (this.getPlayer().hasPermission("advancedperks.gui.setup")) {
            this.addGuiElement(new PerkGuiSetupElement(this), this.perkGuiConfiguration.getSetupGuiSlot());
        }
        this.addGuiElement(new PreviousPageElement(this, this.messagesConfiguration.getComputedString("gui.perk_gui.previous_page")), this.perkGuiConfiguration.getPreviousPageSlot());
        this.addGuiElement(new NextPageElement(this, this.messagesConfiguration.getComputedString("gui.perk_gui.next_page"), this.perkRegistryImpl.getPerks().size() / PERKS_PER_PAGE), this.perkGuiConfiguration.getNextPageSlot());
    }
}
