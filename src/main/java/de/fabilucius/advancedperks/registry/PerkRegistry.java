package de.fabilucius.advancedperks.registry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.perk.PerksConfiguration;
import de.fabilucius.advancedperks.registry.exception.PerkRegistryInitializationException;
import de.fabilucius.advancedperks.registry.loader.PerkYmlLoader;

import java.io.IOException;
import java.util.List;

public class PerkRegistry {

    @Inject
    private PerkYmlLoader perkYmlLoader;

    @Inject
    private ConfigurationProvider configurationProvider;

    @Inject
    private APLogger logger;

    private final Cache<Class<? extends Perk>, Perk> perkCache = CacheBuilder.newBuilder()
            .build();

    //TODO add nullable
    public <T extends Perk> T getPerk(Class<T> perkClass) {
        return perkClass.cast(this.perkCache.getIfPresent(perkClass));
    }

    public void loadAndRegisterDefaultPerks() throws PerkRegistryInitializationException {
        try {
            PerksConfiguration perksConfiguration = this.configurationProvider.getConfigurationAndLoad(PerksConfiguration.class);
            for (Class<? extends AbstractDefaultPerk> perkClass : this.findDefaultPerkClasses()) {
                Perk perk = this.perkYmlLoader.loadPerk(perkClass, perksConfiguration);
                this.logger.info("Successfully loaded the perk %s from %s.".formatted(perk.getIdentifier(), perk.getClass().getName()));
                if (perk.isEnabled()) {
                    this.perkCache.put(perkClass, perk);
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
            return Lists.newArrayList(ClassPath.from(this.getClass().getClassLoader())
                    .getTopLevelClassesRecursive("de.fabilucius.advancedperks").stream()
                    .filter(classInfo -> AbstractDefaultPerk.class.isAssignableFrom(classInfo.load())
                            && !classInfo.load().equals(AbstractDefaultPerk.class))
                    .map(classInfo -> (Class<? extends AbstractDefaultPerk>) classInfo.load())
                    .iterator());
        } catch (IOException exception) {
            throw new PerkRegistryInitializationException("An unexpected io exception was thrown while looping over the plugins classes to find default perks.", exception);
        }
    }
}