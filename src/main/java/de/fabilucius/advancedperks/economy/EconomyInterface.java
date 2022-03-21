package de.fabilucius.advancedperks.economy;

import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;

public interface EconomyInterface {

    PurchaseResult buyPerk(PerkData perkData, Perk perk);

}
