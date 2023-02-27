package de.fabilucius.advancedperks.perks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.NullSafety;
import de.fabilucius.advancedperks.commons.configuration.value.types.SingleValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPerk implements Perk {

    private static final Logger LOGGER = Bukkit.getLogger();
    private final ItemStack icon;
    private final String identifier;
    private final String displayName;
    private final String permission;
    private final List<String> description;
    private final List<String> disabledWorlds;
    private boolean enabled;

    public AbstractPerk(String identifier) {
        this.identifier = identifier;
        PerksConfiguration perksConfiguration = AdvancedPerks.getPerksConfiguration();
        this.displayName = perksConfiguration.getDisplayName(this);
        this.permission = perksConfiguration.getPermission(this);
        this.description = perksConfiguration.getDescription(this);
        this.disabledWorlds = perksConfiguration.getDisabledWorlds(this);
        this.enabled = perksConfiguration.isEnabled(this);
        this.icon = perksConfiguration.getIcon(this);
        this.validatePerkIntegrity();
    }

    public AbstractPerk(String identifier, String displayName, String permission, List<String> description) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.permission = permission;
        this.description = description;
        this.disabledWorlds = Lists.newArrayList();
        this.icon = this.getDefaultIcon();
        this.enabled = true;
        this.validatePerkIntegrity();
    }

    public abstract ItemStack getDefaultIcon();

    private void validatePerkIntegrity() {
        if(this.getIdentifier().contains(",")){
            throw new IllegalStateException("The identifier of a perk isn't allowed to contain a comma in order to make easy database serializing possible.");
        }
        try {
            NullSafety.validateNotNull(this.getDisplayName(), this.getPermission(), this.getDescription(), this.getIcon());
        } catch (NullPointerException exception) {
            this.enabled = false;
            LOGGER.log(Level.SEVERE, String.format("Cannot create instance of perk %s: %s",
                    this.getClass().getName(), exception.getMessage()));
        }
    }

    @Override
    public void perkEnable(Player player) {
    }

    @Override
    public void perkDisable(Player player) {
    }

    @Override
    public void prePerkDisable(Player player) {
        this.perkDisable(player);
    }

    @Override
    public void prePerkEnable(Player player) {
        this.perkEnable(player);
    }

    /* the getter and setter of this class */

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ItemStack getIcon() {
        return this.icon == null ? this.getDefaultIcon() : this.icon;
    }

    @Override
    public SingleValue<Number> getPrice() {
        return new SingleValue<>(AdvancedPerks.getPerksConfiguration(), this.getIdentifier() + ".Price",
                "The amount of currency this perk should cost.", Number.class, -1);
    }
}
