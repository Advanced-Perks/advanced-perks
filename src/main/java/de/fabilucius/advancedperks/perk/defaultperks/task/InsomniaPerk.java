package de.fabilucius.advancedperks.perk.defaultperks.task;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.TaskPerk;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;

import java.util.Map;

@PerkIdentifier("insomnia")
public class InsomniaPerk extends AbstractDefaultPerk implements TaskPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    public InsomniaPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @Override
    public void registerTasks(AdvancedPerks advancedPerks) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(advancedPerks, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
                if (perkData.getEnabledPerks().contains(this)) {
                    player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                }
            });
        }, 0L, 5 * 20L);
    }
}
