package de.fabilucius.advancedperks;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.core.module.PrimaryModule;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class AdvancedPerks extends JavaPlugin {

    @Inject
    private APLogger logger;

    @Inject
    private Injector injector;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new PrimaryModule(this));
        injector.injectMembers(this);
        try {
            this.logger.info("Beginning the bootstrap process of the plugin.");
            this.injector.getInstance(AdvancedPerksBootstrap.class).initializePlugin();
            this.logger.info("Successfully finished the bootstrap process of the plugin.");
        } catch (AdvancedPerksException exception) {
            this.logger.log(Level.SEVERE, "An unexpected error occurred during the bootstrap process of the plugin.", exception);
        }
    }
}
