package de.fabilucius.advancedperks.compatabilities;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.compatabilities.bukkit.ChangeWorldPerkCompatability;
import de.fabilucius.advancedperks.compatabilities.bukkit.PotionDesyncCompatability;
import de.fabilucius.advancedperks.core.logging.APLogger;

@Singleton
public class CompatibilityController {

    @Inject
    private Injector injector;

    @Inject
    private APLogger logger;

    public void registerCompatibilityClasses() {
        this.logger.info("Registering the compatibility class %s.".formatted(this.injector.getInstance(PotionDesyncCompatability.class).getClass().getName()));
        this.logger.info("Registering the compatibility class %s.".formatted(this.injector.getInstance(ChangeWorldPerkCompatability.class).getClass().getName()));
        //TODO Luckperms
    }

}
