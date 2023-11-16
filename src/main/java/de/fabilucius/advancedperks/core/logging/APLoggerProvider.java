package de.fabilucius.advancedperks.core.logging;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.fabilucius.advancedperks.AdvancedPerks;

public class APLoggerProvider implements Provider<APLogger> {

    @Inject
    private AdvancedPerks advancedPerks;

    @Override
    public APLogger get() {
        return new APLogger(advancedPerks.getServer().getLogger());
    }

}
