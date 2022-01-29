package de.fabilucius.advancedperks.perks;

import com.google.common.reflect.ClassPath;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.ListCache;
import de.fabilucius.advancedperks.event.types.PerkRegistryEvent;
import de.fabilucius.sympel.multiversion.ServerVersion;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerkListCache extends ListCache<Perk> {

    private static final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void initialize() {
        Bukkit.getScheduler().runTask(AdvancedPerks.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PerkRegistryEvent(this)));
        this.registerDefaultPerks();
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    private void registerDefaultPerks() {
        try {
            ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
            classPath.getTopLevelClassesRecursive("de.fabilucius.advancedperks.perks.defaultperks").stream().filter(classInfo -> Perk.class.isAssignableFrom(classInfo.load())).forEach(classInfo -> {
                Class<? extends Perk> perkClass = (Class<? extends Perk>) classInfo.load();
                this.registerPerks(perkClass);
            });
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "An error occurred while registering a default perk:", exception);
        }
    }

    public void registerPerks(Class<? extends Perk>... perkClasses) {
        Arrays.stream(perkClasses).forEach(perkClass -> {
            try {
                Perk perk = perkClass.getDeclaredConstructor().newInstance();
                if (perk.isEnabled() && ServerVersion.is(perk.getMinimumServerVersion(), ServerVersion.ComparisonType.LOWER_OR_EQUAL) &&
                        this.isIdentifierUnique(perk.getIdentifier())) {
                    this.getPerks().add(perk);
                }
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, String.format("An error occurred " +
                        "while registering a new perk from the class %s:", perkClass.getSimpleName()), exception);
            }
        });
    }

    public boolean isIdentifierUnique(String identifier) {
        return this.getPerks().stream().noneMatch(perk -> perk.getIdentifier().equalsIgnoreCase(identifier));
    }

    public Perk getPerkByIdentifier(String identifier) {
        Predicate<Perk> byIdentifier = perk -> perk.getIdentifier().equalsIgnoreCase(identifier);
        return this.getEntry(byIdentifier);
    }

    public List<Perk> getPerks() {
        return this.getCache();
    }
}
