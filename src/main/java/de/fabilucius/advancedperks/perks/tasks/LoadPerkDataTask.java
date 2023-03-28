package de.fabilucius.advancedperks.perks.tasks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.perks.PerkStateController;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LoadPerkDataTask implements Runnable {

    private static final PerkStateController PERK_STATE_CONTROLLER = AdvancedPerks.getPerkStateController();
    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private final PerkData perkData;

    public LoadPerkDataTask(PerkData perkData) {
        this.perkData = perkData;
    }
    
    @Override
    public void run() {
        UUID uuid = this.perkData.getPlayer().getUniqueId();
        try {
            ResultSet enabledPerksResultSet = PERK_STATE_CONTROLLER.getAbstractDatabase().selectQuery("enabled_perks", Lists.newArrayList("perk"), "uuid = '" + uuid + "'");
            if (enabledPerksResultSet != null) {
                List<Perk> toEnable = Lists.newArrayList();
                while (enabledPerksResultSet.next()) {
                    for (String perkLine : enabledPerksResultSet.getString("perk").split(",")) {
                        Perk perk = AdvancedPerks.getPerkRegistry().getPerkByIdentifier(perkLine);
                        if (perk != null) {
                            toEnable.add(perk);
                        }
                    }
                }
                /* Load the found perks synchronously */

                Bukkit.getScheduler().runTask(AdvancedPerks.getInstance(),
                        () -> {
                            toEnable.forEach(perk -> {
                                PERK_STATE_CONTROLLER.enablePerk(this.perkData.getPlayer(), perk);
                            });
                        });
            }
            ResultSet unlockedPerksResultSet = PERK_STATE_CONTROLLER.getAbstractDatabase().selectQuery("unlocked_perks",
                    Lists.newArrayList("perk"),
                    "uuid = '" + uuid + "'");
            if (unlockedPerksResultSet != null) {
                while (unlockedPerksResultSet.next()) {
                    List<String> unlockedPerks = Arrays.stream(unlockedPerksResultSet.getString("perk").split(","))
                            .collect(Collectors.toList());
                    this.perkData.getUnlockedPerks().addAll(unlockedPerks);
                }
            }
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "An error occurred while loading the perk data of " + this.perkData.getPlayer().getName()
                    + ": " + sqlException.getMessage());
        }
    }
}