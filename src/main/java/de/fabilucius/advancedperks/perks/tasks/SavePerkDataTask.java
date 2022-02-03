package de.fabilucius.advancedperks.perks.tasks;

import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.sympel.database.AbstractDatabase;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class SavePerkDataTask implements Callable<String> {

    private final String uuid;
    private final String perks;
    private final AbstractDatabase abstractDatabase;

    public SavePerkDataTask(PerkData perkData, AbstractDatabase abstractDatabase) {
        this.uuid = perkData.getPlayer().getUniqueId().toString();
        this.perks = perkData.getActivatedPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
        this.abstractDatabase = abstractDatabase;
    }

    @Override
    public String call() {
        this.getAbstractDatabase().insertOrUpdateQuery("activated_perks",
                Arrays.asList("UUID", "PERKS"),
                Arrays.asList(this.getUuid(), this.getPerks()),
                "UUID = '" + this.getUuid() + "'",
                "PERKS = '" + this.getPerks() + "'");
        return "perk data saved";
    }

    /* the getter and setter of the class */

    protected String getUuid() {
        return uuid;
    }


    protected String getPerks() {
        return perks;
    }

    protected AbstractDatabase getAbstractDatabase() {
        return abstractDatabase;
    }
}
