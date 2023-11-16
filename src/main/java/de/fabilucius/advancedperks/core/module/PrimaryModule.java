package de.fabilucius.advancedperks.core.module;

import com.google.inject.AbstractModule;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.core.logging.APLoggerProvider;

public class PrimaryModule extends AbstractModule {

    private final AdvancedPerks advancedPerks;

    public PrimaryModule(AdvancedPerks advancedPerks) {
        this.advancedPerks = advancedPerks;
    }

    @Override
    protected void configure() {
        bind(AdvancedPerks.class).toInstance(this.advancedPerks);
        bind(APLogger.class).toProvider(APLoggerProvider.class);
    }

}
