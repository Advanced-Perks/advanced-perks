package de.fabilucius.advancedperks.perks.tasks;

import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.sympel.database.AbstractDatabase;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class SavePerkDataTask implements Runnable {

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
        this.getAbstractDatabase().insertOrUpdateQuery("unlocked_perks",
                Arrays.asList("uuid", "perks"),
                Arrays.asList(this.uuid, this.unlockedPerks),
                "uuid ='" + this.uuid + "'",
                "perks = '" + unlockedPerks + "'");
        this.getAbstractDatabase().insertOrUpdateQuery("enabled_perks",
                Arrays.asList("uuid", "perks"),
                Arrays.asList(this.uuid, this.perks),
                "uuid = '" + this.uuid + "'",
                "perks = '" + this.perks + "'");
    }

    /* the getter and setter of the class */

    protected AbstractDatabase getAbstractDatabase() {
        return abstractDatabase;
    }
}
