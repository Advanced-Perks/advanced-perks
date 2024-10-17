package de.fabilucius.advancedperks.api.placeholderapi;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedPerksPerkLimitExpansion extends AbstractAdvancedPerksExpansion {

    @Inject
    private PerkDataRepository perkDataRepository;

    private final SettingsConfiguration settingsConfiguration;

    @Inject
    public AdvancedPerksPerkLimitExpansion(APLogger logger, AdvancedPerks advancedPerks, ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        super(logger, advancedPerks);
        this.settingsConfiguration = configurationLoader.getConfigurationAndLoad(SettingsConfiguration.class);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        int maxPerksAmount = Math.max(perkData.queryMaxPerks(), this.settingsConfiguration.getGlobalMaxActivePerks());
        return String.valueOf(perkData.getEnabledPerks().size() >= maxPerksAmount);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedperks.perk-limit";
    }
}
