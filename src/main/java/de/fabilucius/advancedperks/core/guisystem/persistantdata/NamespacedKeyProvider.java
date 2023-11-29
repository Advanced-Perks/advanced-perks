package de.fabilucius.advancedperks.core.guisystem.persistantdata;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.fabilucius.advancedperks.AdvancedPerks;
import org.bukkit.NamespacedKey;

public class NamespacedKeyProvider implements Provider<NamespacedKey> {

    @Inject
    private AdvancedPerks advancedPerks;

    @Override
    public NamespacedKey get() {
        return new NamespacedKey(this.advancedPerks, "uuid_key");
    }
}
