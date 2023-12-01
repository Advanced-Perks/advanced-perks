package de.fabilucius.advancedperks.data;

import java.util.UUID;

public class UnloadedPerkData extends PerkData {
    public UnloadedPerkData(UUID uuid) {
        super(uuid);
    }

    @Override
    public boolean isLoaded() {
        return false;
    }
}
