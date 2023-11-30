package de.fabilucius.advancedperks.core.economy.interfaces.types;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyInterface;
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyTransactionResult;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.perk.Perk;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

@Singleton
public class VaultEconomyInterface implements EconomyInterface {

    @Inject
    private APLogger logger;

    @Nullable
    private Economy economy = null;

    public VaultEconomyInterface() {
        RegisteredServiceProvider<Economy> economyServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyServiceProvider != null) {
            economy = economyServiceProvider.getProvider();
        }
    }

    @Override
    public EconomyTransactionResult purchasePerk(Player player, Perk perk) {
        if (this.economy == null) {
            //TODO maybe change it in a startup exception/error
            return EconomyTransactionResult.NO_ECONOMY_PROVIDER;
        }
        if (this.economy.getBalance(player) < perk.getPrice().get()) {
            return EconomyTransactionResult.NOT_ENOUGH_FUNDS;
        }
        EconomyResponse economyResponse = this.economy.withdrawPlayer(player, perk.getPrice().get());
        return switch (economyResponse.type) {
            case FAILURE, NOT_IMPLEMENTED -> {
                this.logger.severe("Unable to process economy transaction %s with error message %s.".formatted(economyResponse.type, economyResponse.errorMessage));
                yield EconomyTransactionResult.ERROR;
            }
            default -> EconomyTransactionResult.SUCCESS;
        };
    }
}
