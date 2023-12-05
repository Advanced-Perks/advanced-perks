package de.fabilucius.advancedperks.core.database.types;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.database.AbstractDatabase;
import de.fabilucius.advancedperks.core.database.Credentials;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perk.Perk;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class RemoteDatabase extends AbstractDatabase {

    @Inject
    private APLogger logger;

    protected RemoteDatabase(String connectionUrl, Credentials credentials) {
        super(connectionUrl, credentials);
    }

    public static RemoteDatabase withoutCredentials(String connectionUrl) {
        Preconditions.checkState(!Strings.isNullOrEmpty(connectionUrl), "connectionUrl cannot be null or empty");
        return new RemoteDatabase(connectionUrl, Credentials.withoutAuth());
    }

    public static RemoteDatabase withCredentials(String connectionUrl, Credentials credentials) {
        Preconditions.checkState(!Strings.isNullOrEmpty(connectionUrl), "connectionUrl cannot be null or empty");
        Preconditions.checkNotNull(credentials, "credentials cannot be null");
        return new RemoteDatabase(connectionUrl, credentials);
    }

    @Override
    public void savePerkData(PerkData perkData) {
        String saveQuery = "INSERT INTO ap_data(unique_id, enabled_perks, bought_perks, data_hash) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE enabled_perks = ?, bought_perks = ?, data_hash = ?";
        try (PreparedStatement saveStatement = this.createPreparedStatement(saveQuery)) {
            String enabledPerks = perkData.getEnabledPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
            String boughtPerks = String.join(",", perkData.getBoughtPerks());
            saveStatement.setString(1, perkData.getUuid().toString());
            saveStatement.setString(2, enabledPerks);
            saveStatement.setString(3, boughtPerks);
            saveStatement.setBytes(4, perkData.calculateDataHash());
            saveStatement.setString(5, enabledPerks);
            saveStatement.setString(6, boughtPerks);
            saveStatement.setBytes(7, perkData.calculateDataHash());
            saveStatement.execute();
        } catch (SQLException exception) {
            this.logger.log(Level.WARNING, "An error occurred while saving the PerkData for uniqueId %s.".formatted(perkData.getUuid().toString()), exception);
        }
    }
}
