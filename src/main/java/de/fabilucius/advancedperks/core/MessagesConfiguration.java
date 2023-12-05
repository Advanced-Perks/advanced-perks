package de.fabilucius.advancedperks.core;

import de.fabilucius.advancedperks.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.configuration.types.AbstractMessageConfiguration;

@FilePathInJar("messages.yml")
public class MessagesConfiguration extends AbstractMessageConfiguration {

    @Override
    public String getPrefix() {
        return this.getString("prefix", "key for the 'prefix' wasn't found.");
    }
}
