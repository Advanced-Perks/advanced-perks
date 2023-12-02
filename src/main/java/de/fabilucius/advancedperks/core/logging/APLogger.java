package de.fabilucius.advancedperks.core.logging;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.AdvancedPerks;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Singleton
public class APLogger extends Logger {

    @Inject
    public APLogger(AdvancedPerks plugin) {
        super("advanced-perks logger", null);
        setLevel(Level.ALL);
        setParent(plugin.getServer().getLogger());
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage("[ap] " + logRecord.getMessage());
        super.log(logRecord);
    }
}
