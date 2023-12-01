package de.fabilucius.advancedperks.registry.loader;

import de.fabilucius.advancedperks.perk.Perk;

public interface PerkLoader<S, E extends Exception> {

    <T extends Perk> T loadPerk(Class<T> perkClass, S from) throws E;

}
