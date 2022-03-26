package de.fabilucius.advancedperks.perks.tasks;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.sympel.database.AbstractDatabase;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SavePerkDataTask implements Runnable {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private final String uuid;
    private final String perks;
    private final String unlockedPerks;
    private final AbstractDatabase abstractDatabase;

    public SavePerkDataTask(PerkData perkData, AbstractDatabase abstractDatabase) {
        this.uuid = perkData.getPlayer().getUniqueId().toString();
        this.perks = perkData.getActivatedPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
        this.unlockedPerks = String.join(",", perkData.getUnlockedPerks());
        this.abstractDatabase = abstractDatabase;
    }

    public void run() {
        try {
            this.getAbstractDatabase().insertOrUpdateQuery("unlocked_perks",
                    Arrays.asList("uuid", "perk"),
                    Arrays.asList(this.uuid, this.unlockedPerks),
                    "uuid ='" + this.uuid + "'",
                    "perk = '" + unlockedPerks + "'");
            this.getAbstractDatabase().insertOrUpdateQuery("enabled_perks",
                    Arrays.asList("uuid", "perk"),
                    Arrays.asList(this.uuid, this.perks),
                    "uuid = '" + this.uuid + "'",
                    "perk = '" + this.perks + "'");
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "Unable to save perk data from " + this.uuid + ": " + sqlException.getMessage());
        }
    }

    /* the getter and setter of the class */

    protected AbstractDatabase getAbstractDatabase() {
        return abstractDatabase;
    }
}
