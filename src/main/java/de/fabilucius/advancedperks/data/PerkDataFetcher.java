package de.fabilucius.advancedperks.data;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.perks.Perk;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerkDataFetcher {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    public static CompletableFuture<List<String>> fetchBoughtPerksByUuid(UUID uuid) {
        CompletableFuture<List<String>> fetchFuture = new CompletableFuture<>();
        AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataByUuid(uuid).ifPresentOrElse(perkData -> {
            fetchFuture.complete(perkData.getUnlockedPerks());
        }, () -> {
            Bukkit.getScheduler().runTaskAsynchronously(AdvancedPerks.getInstance(), () -> {
                List<String> fetchedBoughtPerks = Lists.newArrayList();
                try {
                    ResultSet resultSet = AdvancedPerks.getInstance().getPerkStateController().getAbstractDatabase()
                            .selectQuery("unlocked_perks", Lists.newArrayList("perk"), "uuid = '" + uuid + "'");
                    fetchedBoughtPerks.addAll(mapResultSet(resultSet,
                            string -> string));
                } catch (SQLException sqlException) {
                    LOGGER.log(Level.WARNING, "There was an error while fetching the bought perks of the uuid %s."
                            .formatted(uuid), sqlException);
                } finally {
                    fetchFuture.complete(fetchedBoughtPerks);
                }
            });
        });
        return fetchFuture.orTimeout(1, TimeUnit.MINUTES).exceptionally(throwable -> Lists.newArrayList());
    }

    public static CompletableFuture<List<Perk>> fetchEnabledPerksByUuid(UUID uuid) {
        CompletableFuture<List<Perk>> fetchFuture = new CompletableFuture<>();
        AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataByUuid(uuid).ifPresentOrElse(perkData -> {
            fetchFuture.complete(perkData.getActivatedPerks());
        }, () -> {
            Bukkit.getScheduler().runTaskAsynchronously(AdvancedPerks.getInstance(), () -> {
                List<Perk> fetchedPerks = Lists.newArrayList();
                try {
                    ResultSet resultSet = AdvancedPerks.getInstance().getPerkStateController().getAbstractDatabase()
                            .selectQuery("enabled_perks", Lists.newArrayList("perk"), "uuid = '" + uuid + "'");
                    fetchedPerks.addAll(mapResultSet(resultSet,
                            string -> AdvancedPerks.getInstance().getPerkRegistry().getPerkByIdentifier(string)));
                } catch (SQLException sqlException) {
                    LOGGER.log(Level.WARNING, "There was an error while fetching the enabled perks of the uuid %s."
                            .formatted(uuid), sqlException);
                } finally {
                    fetchFuture.complete(fetchedPerks);
                }
            });
        });
        return fetchFuture.orTimeout(1, TimeUnit.MINUTES).exceptionally(throwable -> Lists.newArrayList());
    }

    private static <T> List<T> mapResultSet(@Nullable ResultSet resultSet, Function<String, T> mapper) throws SQLException {
        if (resultSet == null) {
            return Lists.newArrayList();
        } else {
            List<T> outputList = Lists.newArrayList();
            while (resultSet.next()) {
                for (String line : resultSet.getString("perk").split(",")) {
                    T mappedLine = mapper.apply(line);
                    if (mappedLine != null) {
                        outputList.add(mappedLine);
                    }
                }
            }
            return outputList;
        }
    }

}
