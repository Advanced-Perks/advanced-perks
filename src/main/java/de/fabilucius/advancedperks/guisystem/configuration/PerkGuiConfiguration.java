package de.fabilucius.advancedperks.guisystem.configuration;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.core.configuration.Configuration;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.guisystem.setupperkgui.SetupPerkGuiWindow;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.CloseGuiPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.DisableAllPerksPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.NextPagePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PerkIconPlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PerkTogglePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.PreviousPagePlaceholderElement;
import de.fabilucius.advancedperks.guisystem.setupperkgui.elements.SetupGuiPlaceholderElement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@FilePathInJar("perk_gui.yml")
public class PerkGuiConfiguration extends Configuration {

    //TODO currently uneeded completely change the saving spot
    public PerkGuiSaveResult savePerkGuiLayout(SetupPerkGuiWindow guiWindow) {
        if (guiWindow.getGuiElements().size() != 21 && guiWindow.getGuiElements().size() != 54) {
            return PerkGuiSaveResult.MISSING_ELEMENTS;
        }
        try {
            guiWindow.getGuiElements().values().stream()
                    .filter(guiElement -> !(guiElement instanceof PerkIconPlaceholderElement) && !(guiElement instanceof PerkTogglePlaceholderElement))
                    .forEach(guiElement -> {
                        if (guiElement instanceof CloseGuiPlaceholderElement) {
                            this.set("close_gui", guiWindow.getSlot(guiElement));
                        } else if (guiElement instanceof DisableAllPerksPlaceholderElement) {
                            this.set("disable_all_perks", guiWindow.getSlot(guiElement));
                        } else if (guiElement instanceof SetupGuiPlaceholderElement) {
                            this.set("setup_gui", guiWindow.getSlot(guiElement));
                        } else if (guiElement instanceof NextPagePlaceholderElement) {
                            this.set("next_page", guiWindow.getSlot(guiElement));
                        } else if (guiElement instanceof PreviousPagePlaceholderElement) {
                            this.set("previous_page", guiWindow.getSlot(guiElement));
                        }
                    });
            List<Integer> iconSlots = guiWindow.getGuiElements().values().stream()
                    .filter(PerkIconPlaceholderElement.class::isInstance)
                    .map(PerkIconPlaceholderElement.class::cast)
                    .sorted(Comparator.comparingInt(PerkIconPlaceholderElement::getIndex))
                    .map(guiWindow::getSlot)
                    .toList();
            List<Integer> toggleSlots = guiWindow.getGuiElements().values().stream()
                    .filter(PerkTogglePlaceholderElement.class::isInstance)
                    .map(PerkTogglePlaceholderElement.class::cast)
                    .sorted(Comparator.comparingInt(PerkTogglePlaceholderElement::getIndex))
                    .map(guiWindow::getSlot)
                    .toList();
            if (iconSlots.contains(-1) || toggleSlots.contains(-1)) {
                return PerkGuiSaveResult.MISSING_ELEMENTS;
            }
            this.set("perk_icon_locations", IntStream.range(0, iconSlots.size()).mapToObj(value -> iconSlots.get(value) + ":" + toggleSlots.get(value)).toList());
            this.set("background", guiWindow.hasBackground());
            this.saveConfiguration();
            return PerkGuiSaveResult.SUCCESS;
        } catch (Exception exception) {
            exception.printStackTrace();
            return PerkGuiSaveResult.ERROR;
        }
    }

    public List<PerkIconLocation> getPerkIconLocations() {
        try {
            List<PerkIconLocation> perkIconLocations = this.getStringList("perk_icon_locations").stream()
                    .map(s -> s.split(":"))
                    .map(strings -> {
                        int iconSlot = Integer.parseInt(strings[0]);
                        int toggleSlot = Integer.parseInt(strings[1]);
                        return new PerkIconLocation(iconSlot, toggleSlot);
                    }).toList();
            return perkIconLocations.size() != 8 ? this.getDefaultPerkIconLocations() : perkIconLocations;
        } catch (Exception exception) {
            return this.getDefaultPerkIconLocations();
        }
    }

    private List<PerkIconLocation> getDefaultPerkIconLocations() {
        List<Integer> iconLocations = Lists.newArrayList(1, 3, 5, 7, 19, 21, 23, 25);
        List<Integer> toggleLocations = Lists.newArrayList(10, 12, 14, 16, 28, 30, 32, 34);
        return IntStream.range(0, iconLocations.size())
                .mapToObj(value -> new PerkIconLocation(iconLocations.get(value), toggleLocations.get(value)))
                .toList();
    }

    public int getCloseGuiSlot() {
        return this.getInt("close_gui", 46);
    }

    public int getDisableAllPerksSlot() {
        return this.getInt("disable_all_perks", 48);
    }

    public int getSetupGuiSlot() {
        return this.getInt("setup_gui", 49);
    }

    public int getPreviousPageSlot() {
        return this.getInt("previous_page", 50);
    }

    public int getNextPageSlot() {
        return this.getInt("next_page", 52);
    }

    public boolean hasBackground() {
        return this.getBoolean("background", true);
    }

}

