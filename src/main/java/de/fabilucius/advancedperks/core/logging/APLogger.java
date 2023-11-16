package de.fabilucius.advancedperks.core.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class APLogger extends Logger {

    public APLogger(Logger logger) {
        super("advanced-perks logger", null);
        setLevel(Level.ALL);
        setParent(logger);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage("[ap] " + record.getMessage());
        super.log(record);
    }
}
