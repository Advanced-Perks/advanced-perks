package de.fabilucius.advancedperks.economy;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.economy.types.VaultEconomyInterface;
import de.fabilucius.advancedperks.exception.EconomyInterfaceInitializationException;
import de.fabilucius.advancedperks.perks.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EconomyController {

    private static EconomyController instance;

    @Nullable
    public static EconomyController getSingleton() {
        if (instance == null) {
            try {
                instance = new EconomyController();
            } catch (EconomyInterfaceInitializationException exception) {
                LOGGER.log(Level.WARNING, "There was an error while loading the economy controller: " +
                        exception.getMessage());
                LOGGER.log(Level.WARNING, "All economy based features will be disabled.");
            }
        }
        return instance;
    }

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private final EconomyInterface economyInterface;

    private EconomyController() throws EconomyInterfaceInitializationException {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            this.economyInterface = new VaultEconomyInterface();
        } else {
            throw new EconomyInterfaceInitializationException("Theres was no valid economy interface found.");
        }
    }

    public PurchaseResult buyPerk(Player player, Perk perk) {
        if (perk.getPrice().get().doubleValue() < 0) {
            return PurchaseResult.NO_VALID_PRICE;
        }
        PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(player);
        if (perkData == null) {
            LOGGER.log(Level.WARNING, "Purchase of perk "
                    + perk.getIdentifier()
                    + " for the player "
                    + player.getName()
                    + " wasn't successful, the player doesnt have valid perk data.");
            return PurchaseResult.ERROR;
        }
        if (perkData.isPerkUnlocked(perk)) {
            return PurchaseResult.ALREADY_OWNS_PERK;
        }
        PurchaseResult result = this.economyInterface.buyPerk(perkData, perk);
        if (result.equals(PurchaseResult.SUCCESS)) {
            perkData.getUnlockedPerks().add(perk.getIdentifier());
        }
        return result;
    }

}
