package de.fabilucius.advancedperks.api;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.Singleton;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.exception.PerkRegisterException;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.perks.PerkListCache;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AdvancedPerksApi {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();
    /* References to the for the Api important endpoints inside the plugin */
    private static final PerkListCache PERK_LIST_CACHE = AdvancedPerks.getPerkRegistry();
    private static final PerkDataRepository PERK_DATA_REPOSITORY = AdvancedPerks.getPerkDataRepository();

    /**
     * This method will try to register the as parameter passed class extending {@link Perk}.
     * If it is successful the {@link RegisterResponse} is SUCCESS if not its ERROR and you can take a look at the console to see what failed.
     *
     * @param perk the perk class that should be registered
     * @return either SUCCESS when it was successful or ERROR if not
     */
    public RegisterResponse registerPerk(Class<Perk> perk) {
        try {
            PERK_LIST_CACHE.registerPerks(perk);
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
        return PERK_DATA_REPOSITORY.getPerkData(player);
    }

    /* Singleton stuff */

    private static AdvancedPerksApi instance;

    private AdvancedPerksApi() {
    }

    public AdvancedPerksApi getInstance() {
        if (instance == null) {
            instance = new AdvancedPerksApi();
        }
        return instance;
    }
}
