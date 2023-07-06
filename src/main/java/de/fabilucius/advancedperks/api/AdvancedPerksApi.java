package de.fabilucius.advancedperks.api;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.exception.PerkRegisterException;
import de.fabilucius.advancedperks.perks.Perk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancedPerksApi {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    /**
     * This method will try to register the as parameter passed class extending {@link Perk}.
     * If it is successful the {@link RegisterResponse} is SUCCESS if not its ERROR and you can take a look at the console to see what failed.
     *
     * @param perk the perk class that should be registered
     * @return either SUCCESS when it was successful or ERROR if not
     */
    public RegisterResponse registerPerk(Class<? extends Perk> perk) {
        try {
            AdvancedPerks.getPerkRegistry().registerPerks(perk);
            return RegisterResponse.SUCCESS;
        } catch (PerkRegisterException exception) {
            LOGGER.log(Level.WARNING, "Unable to register a perk with the Api:", exception);
            return RegisterResponse.ERROR;
        }
    }

    /**
     * This method will return a DTO wrapped around the passed player from the constructor.
     * The {@link PerkData} class holds almost any interesting properties regarding the player and its interaction with the perks.
     * For example, it holds which perks are enabled by the player or how many perks at once he can enable.
     *
     * @param player the player the data object is about
     * @return the data object of the player
     */
    @NotNull
    public PerkData getPerkData(Player player) {
        return AdvancedPerks.getPerkDataRepository().getPerkData(player);
    }

    /* Singleton stuff */

    private static AdvancedPerksApi instance;

    private AdvancedPerksApi() {
    }

    public static AdvancedPerksApi getInstance() {
        if (instance == null) {
            instance = new AdvancedPerksApi();
        }
        return instance;
    }
}
