package de.fabilucius.advancedperks.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.configuration.type.PerksConfiguration;
import de.fabilucius.advancedperks.core.datastructure.DualKeyMap;
import de.fabilucius.advancedperks.core.datastructure.DualKeyMaps;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import de.fabilucius.advancedperks.perk.types.TaskPerk;
import de.fabilucius.advancedperks.registry.exception.PerkRegistryInitializationException;
import de.fabilucius.advancedperks.registry.loader.PerkYmlLoader;
import de.fabilucius.advancedperks.registry.model.SetPriceResult;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Singleton
public class PerkRegistryImpl implements PerkRegistry {

    @Inject
    private PerkYmlLoader perkYmlLoader;

    @Inject
    private APLogger logger;

    @Inject
    private AdvancedPerks advancedPerks;

    @Inject
    private PerksConfiguration perksConfiguration;

    private final DualKeyMap<String, Class<? extends Perk>, Perk> perkCache = DualKeyMaps.newDualKeyMap();

    @Nullable
    public <T extends Perk> T getPerk(Class<T> perkClass) {
        return perkClass.cast(this.perkCache.getBySecondKey(perkClass));
    }

    @Nullable
    public Perk getPerkByIdentifier(String identifier) {
        if (Strings.isNullOrEmpty(identifier)) {
            return null;
        }
        return this.perkCache.getByFirstKey(identifier);
    }

    @Override
    public @NotNull SetPriceResult setPrice(Perk perk, Double price) {
        Map<String, Object> flags = perksConfiguration.setPrice(perk.getIdentifier(), price);
        perksConfiguration.saveConfiguration();
        perk.refreshPerkFlags(flags);
        return SetPriceResult.PRICE_SET;
    }

    public void loadAndRegisterDefaultPerks() throws PerkRegistryInitializationException {
        try {
            for (Class<? extends AbstractDefaultPerk> perkClass : this.findDefaultPerkClasses()) {
                Perk perk = this.perkYmlLoader.loadPerk(perkClass, perksConfiguration);
                this.logger.info("Successfully loaded the perk %s from %s.".formatted(perk.getIdentifier(), perk.getClass().getName()));
                if (perk.isEnabled()) {
                    this.perkCache.putValue(perk.getIdentifier(), perkClass, perk);
                    if (perk instanceof ListenerPerk listenerPerk) {
                        Bukkit.getPluginManager().registerEvents(listenerPerk, this.advancedPerks);
                    }
                    if (perk instanceof TaskPerk taskPerk) {
                        taskPerk.registerTasks(this.advancedPerks);
                    }
                } else {
                    this.logger.info("The perk wasn't loaded into the cache because its set to disabled in the perks.yml file.");
                }
            }
        } catch (AdvancedPerksException exception) {
            throw new PerkRegistryInitializationException("An unexpected error occurred while loading and registering the default perks.", exception);
        }
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    private List<Class<? extends AbstractDefaultPerk>> findDefaultPerkClasses() throws PerkRegistryInitializationException {
        try {
            return Lists.newArrayList(ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive("de.fabilucius.advancedperks").stream().filter(classInfo -> {
                try {
                    return AbstractDefaultPerk.class.isAssignableFrom(classInfo.load()) && !classInfo.load().equals(AbstractDefaultPerk.class);
                } catch (NoClassDefFoundError error) {
                    /* This will get triggered by stuff like PlaceholderAPI not running but still getting referenced in classes around the plugin */
                    return false;
                }
            }).map(classInfo -> (Class<? extends AbstractDefaultPerk>) classInfo.load()).iterator());
        } catch (IOException exception) {
            throw new PerkRegistryInitializationException("An unexpected io exception was thrown while looping over the plugins classes to find default perks.", exception);
        }
    }

    @NotNull
    public List<Perk> getPerks() {
        return this.perkCache.values().stream().toList();
    }

}
