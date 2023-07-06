package de.fabilucius.advancedperks.compatability;

import com.google.common.reflect.ClassPath;
import de.fabilucius.advancedperks.AdvancedPerks;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CompatabilityController {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private CompatabilityController() {
        this.loadCompatabilityEntities();
    }

    @SuppressWarnings({"UnstableApiUsage"})
    private void loadCompatabilityEntities() {
        try {
            List<Class<?>> compatClasses = ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive("de.fabilucius.advancedperks.compatability.compats").stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(CompatabilityEntity.class::isAssignableFrom)
                    .collect(Collectors.toList());

            for (Class<?> compatClass : compatClasses) {
                compatClass.getConstructor().newInstance();
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "An error occurred while registering a compatability class:", exception);
        }
    }

    /* Singleton stuff */

    private static CompatabilityController instance;

    public static CompatabilityController getSingleton() {
        if (instance == null) {
            instance = new CompatabilityController();
        }
        return instance;
    }

}
