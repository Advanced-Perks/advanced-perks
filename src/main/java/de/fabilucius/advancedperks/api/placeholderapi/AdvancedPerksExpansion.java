package de.fabilucius.advancedperks.api.placeholderapi;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.logging.APLogger;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public abstract class AdvancedPerksExpansion extends PlaceholderExpansion {

    private final AdvancedPerks advancedPerks;

    @Inject
    public AdvancedPerksExpansion(APLogger logger, AdvancedPerks advancedPerks) {
        this.advancedPerks = advancedPerks;
        this.register();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(",", this.advancedPerks.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return this.advancedPerks.getDescription().getVersion();
    }
}
