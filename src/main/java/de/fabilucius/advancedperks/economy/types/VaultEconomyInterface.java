package de.fabilucius.advancedperks.economy.types;

import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.economy.EconomyInterface;
import de.fabilucius.advancedperks.economy.PurchaseResult;
import de.fabilucius.advancedperks.exception.EconomyInterfaceInitializationException;
import de.fabilucius.advancedperks.perks.Perk;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VaultEconomyInterface implements EconomyInterface {

    private static final Logger LOGGER = Bukkit.getLogger();

    private final Economy economy;

    public VaultEconomyInterface() throws EconomyInterfaceInitializationException {
        RegisteredServiceProvider<Economy> serviceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (serviceProvider == null) {
            throw new EconomyInterfaceInitializationException("The economy service provider is null.");
        }
        this.economy = serviceProvider.getProvider();
    }

    @Override
    public PurchaseResult buyPerk(PerkData perkData, Perk perk) {
        if (this.economy.getBalance(perkData.getPlayer()) < perk.getPrice().get().doubleValue()) {
            return PurchaseResult.NOT_ENOUGH_FUNDS;
        }
        EconomyResponse economyResponse = this.economy.withdrawPlayer(perkData.getPlayer(), perk.getPrice().get().doubleValue());
        switch (economyResponse.type) {
            case FAILURE:
            case NOT_IMPLEMENTED:
                LOGGER.log(Level.WARNING, "Unable to process economy transaction, " + economyResponse.type + " with " + economyResponse.errorMessage);
                return PurchaseResult.ERROR;
            default:
                return PurchaseResult.SUCCESS;
        }
    }
}
