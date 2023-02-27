package de.fabilucius.advancedperks.perks;

import com.google.common.reflect.ClassPath;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.ListCache;
import de.fabilucius.advancedperks.commons.Singleton;
import de.fabilucius.advancedperks.event.types.PerkRegistryEvent;
import de.fabilucius.advancedperks.exception.PerkRegisterException;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

@Singleton("For obvious reasons regarding the structure of the plugin there should only ever be one instance of this class.")
public class PerkListCache extends ListCache<Perk> {

    private static final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void initialize() {
        Bukkit.getScheduler().runTask(AdvancedPerks.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PerkRegistryEvent(this)));
        this.registerDefaultPerks();
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    private void registerDefaultPerks() throws PerkRegisterException {
        try {
            ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
            classPath.getTopLevelClassesRecursive("de.fabilucius.advancedperks.perks.defaultperks").stream().filter(classInfo -> Perk.class.isAssignableFrom(classInfo.load())).forEach(classInfo -> {
                Class<? extends Perk> perkClass = (Class<? extends Perk>) classInfo.load();
                this.registerPerks(perkClass);
            });
        } catch (Exception exception) {
            throw new PerkRegisterException("An error occurred while trying to register the default perks recursively.", exception);
        }
    }

    public void registerPerks(Class<? extends Perk>... perkClasses) throws PerkRegisterException {
        Arrays.stream(perkClasses).forEach(perkClass -> {
            try {
                Perk perk = perkClass.getDeclaredConstructor().newInstance();
                if (perk.isEnabled() && this.isIdentifierUnique(perk.getIdentifier())) {
                    this.getPerks().add(perk);
                }
            } catch (Exception exception) {
                throw new PerkRegisterException(String.format("An error occurred while registering a new perk from the class %s:", perkClass.getName()), exception);
            }
        });
    }

    public boolean isIdentifierUnique(String identifier) {
        return this.getPerks().stream().noneMatch(perk -> perk.getIdentifier().equalsIgnoreCase(identifier));
    }

    @Nullable
    public Perk getPerkByIdentifier(String identifier) {
        Predicate<Perk> byIdentifier = perk -> perk.getIdentifier().equalsIgnoreCase(identifier);
        return this.getEntry(byIdentifier);
    }

    public List<Perk> getPerks() {
        return this.getCache();
    }

    /* Singleton stuff */

    private static PerkListCache instance;

    public static PerkListCache getSingleton() {
        if (instance == null) {
            instance = new PerkListCache();
        }
        return instance;
    }

    private PerkListCache() {
    }

}
