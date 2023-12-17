package de.fabilucius.advancedperks.registry;

import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.model.SetPriceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PerkRegistry {

    @NotNull SetPriceResult setPrice(Perk perk, Double price);

    @Nullable <T extends Perk> T getPerk(Class<T> perkClass);

    @Nullable
    Perk getPerkByIdentifier(String identifier);

    @NotNull
    List<Perk> getPerks();

}
