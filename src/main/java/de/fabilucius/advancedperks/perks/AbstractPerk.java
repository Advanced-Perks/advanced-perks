package de.fabilucius.advancedperks.perks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.NullSafety;
import de.fabilucius.sympel.multiversion.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractPerk implements Perk {

    private static final Logger LOGGER = Bukkit.getLogger();
    private final ItemStack icon;
    private ServerVersion minimumServerVersion = ServerVersion.v1_8;
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
        icon = perksConfiguration.getIcon(this);
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
        if (NullSafety.isAnyNull(this.getDisplayName(), this.getPermission(), this.getDescription(), this.getIcon())) {
            this.enabled = false;
            LOGGER.log(Level.SEVERE, String.format("Cannot create instance of perk %s, the following " +
                    "fields are null:" + Arrays.stream(this.getClass().getDeclaredFields()).filter(field -> {
                        try {
                            return field.get(this) == null;
                        } catch (Exception exception) {
                            LOGGER.log(Level.SEVERE, String.format("Error while " +
                                    "validating the integrity of perk %s:", this.getIdentifier()), exception);
                            return false;
                        }
                    })
                    .map(Field::getName)
                    .collect(Collectors.joining(",")), this.getIdentifier()));
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
    public ServerVersion getMinimumServerVersion() {
        return minimumServerVersion;
    }

    @Override
    public void setMinimumServerVersion(ServerVersion minimumServerVersion) {
        this.minimumServerVersion = minimumServerVersion;
    }
}
