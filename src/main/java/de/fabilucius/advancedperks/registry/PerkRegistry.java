package de.fabilucius.advancedperks.registry;

import de.fabilucius.advancedperks.perk.Perk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PerkRegistry {

    @Nullable <T extends Perk> T getPerk(Class<T> perkClass);

    @Nullable
    Perk getPerkByIdentifier(String identifier);

    @NotNull
    List<Perk> getPerks();

}
