package de.fabilucius.advancedperks.commons.configuration.factory;

import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.commons.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigSingletonFactory {

    private static final Logger LOGGER = JavaPlugin.getProvidingPlugin(ConfigSingletonFactory.class).getLogger();

    private static final Map<Class<? extends Config>, Config> CONFIG_INSTANCES = Maps.newHashMap();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Config> T createConfiguration(Class<T> configClass) {
        return (T) CONFIG_INSTANCES.computeIfAbsent(configClass, configurationClass -> instanceFromClass(configClass));
    }

    public static <T extends Config> T reloadConfiguration(Class<T> configClass) {
        T config = instanceFromClass(configClass);
        CONFIG_INSTANCES.put(configClass, config);
        return config;
    }

    @Nullable
    private static <T extends Config> T instanceFromClass(Class<T> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Couldn't create an instance of the config " + configClass.getName() + ": ", exception);
            return null;
        }
    }

}
