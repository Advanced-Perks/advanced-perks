package de.fabilucius.advancedperks.core.economy;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyInterface;
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyTransactionResult;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class EconomyController {

    @Inject
    @Nullable
    private EconomyInterface economyInterface;

    @Inject
    private PerkDataRepository perkDataRepository;

    public PerkBuyResult buyPerk(Player player, Perk perk) {
        if (economyInterface == null) {
            return PerkBuyResult.NO_ECONOMY_INTERFACE;
        }
        if (perk.getPrice().isEmpty()) {
            return PerkBuyResult.NO_PRICE_SET;
        }
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getBoughtPerks().contains(perk.getIdentifier())) {
            return PerkBuyResult.ALREADY_BOUGHT_PERK;
        }
        EconomyTransactionResult economyTransactionResult = this.economyInterface.purchasePerk(player, perk);
        switch (economyTransactionResult) {
            case NO_ECONOMY_PROVIDER -> {
                return PerkBuyResult.NO_ECONOMY_INTERFACE;
            }
            case NOT_ENOUGH_FUNDS -> {
                return PerkBuyResult.NOT_ENOUGH_FUNDS;
            }
            case ERROR -> {
                return PerkBuyResult.ERROR;
            }
            default -> {
                perkData.getBoughtPerks().add(perk.getIdentifier());
                return PerkBuyResult.SUCCESS;
            }
        }
    }

}
