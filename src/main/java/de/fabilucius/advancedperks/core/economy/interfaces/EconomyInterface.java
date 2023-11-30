package de.fabilucius.advancedperks.core.economy.interfaces;

import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

public interface EconomyInterface {

    EconomyTransactionResult purchasePerk(Player player, Perk perk);

}
