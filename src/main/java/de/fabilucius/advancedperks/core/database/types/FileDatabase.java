package de.fabilucius.advancedperks.core.database.types;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.database.AbstractDatabase;
import de.fabilucius.advancedperks.core.database.Credentials;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FileDatabase extends AbstractDatabase {

    private static final Logger OLD_LOGGER = Bukkit.getLogger();

    @Inject
    private APLogger logger;

    protected FileDatabase(String connectionUrl) {
        super(connectionUrl, Credentials.withoutAuth());
    }

    public static FileDatabase fromFile(File file) {
        Preconditions.checkNotNull(file, "file cannot be null");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                OLD_LOGGER.log(Level.SEVERE, "There was an error while creating the file for a database:", ioException);
            }
        }
        return new FileDatabase("jdbc:sqlite:" + file.getPath());
    }

    @Override
    public void savePerkData(PerkData perkData) {
        String saveQuery = "INSERT OR REPLACE INTO ap_data (unique_id, enabled_perks, bought_perks, data_hash) VALUES (?, ?, ?, ?)";
        try (PreparedStatement saveStatement = this.createPreparedStatement(saveQuery)) {
            String enabledPerks = perkData.getEnabledPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
            String boughtPerks = String.join(",", perkData.getBoughtPerks());
            saveStatement.setString(1, perkData.getUuid().toString());
            saveStatement.setString(2, enabledPerks);
            saveStatement.setString(3, boughtPerks);
            saveStatement.setBytes(4, perkData.calculateDataHash());
            saveStatement.execute();
        } catch (SQLException exception) {
            this.logger.log(Level.WARNING, "An error occurred while saving the PerkData for uniqueId %s.".formatted(perkData.getUuid().toString()), exception);
        }
    }

    @Override
    public boolean runPerkDataMigrateScript() {
        return this.runSqlScript("sql/perk_data_migration_sqlite.sql");
    }

}
