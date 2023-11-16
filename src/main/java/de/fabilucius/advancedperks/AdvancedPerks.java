package de.fabilucius.advancedperks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.core.module.PrimaryModule;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedPerks extends JavaPlugin {

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new PrimaryModule(this));
        injector.injectMembers(this);
    }
}
