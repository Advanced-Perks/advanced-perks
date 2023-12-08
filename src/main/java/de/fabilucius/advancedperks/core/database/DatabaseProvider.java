package de.fabilucius.advancedperks.core.database;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.database.exception.DataFileCreationException;
import de.fabilucius.advancedperks.core.database.types.FileDatabase;
import de.fabilucius.advancedperks.core.database.types.RemoteDatabase;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import jakarta.inject.Named;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DatabaseProvider implements Provider<Database> {

    @Inject
    private APLogger logger;

    @Inject
    @Named("configurationDirectory")
    private File configurationDirectory;

    @Inject
    private ConfigurationLoader configurationLoader;

    @Inject
    private Injector injector;

    @Override
    public Database get() {
        try {
            DatabaseConfiguration databaseConfiguration = this.configurationLoader.getConfigurationAndLoad(DatabaseConfiguration.class);
            switch (databaseConfiguration.getSaveType()) {
                case FILE -> {
                    return this.createFileDatabaseConnection(databaseConfiguration.getFileLocation());
                }
                case DATABASE -> {
                    RemoteDatabase remoteDatabase = RemoteDatabase.withCredentials(databaseConfiguration.getSqlUrl(), databaseConfiguration.getCredentials());
                    this.injector.injectMembers(remoteDatabase);
                    return remoteDatabase;
                }
                default -> {
                    this.logger.warning("The save_type in the database.yml file has to be set to either 'FILE' or 'DATABASE' for now the plugin will use the default case of 'FILE'.");
                    return this.createFileDatabaseConnection(databaseConfiguration.getFileLocation());
                }
            }
        } catch (AdvancedPerksException exception) {
            this.logger.log(Level.SEVERE, "Unable to retrieve a database instance from the database provider.", exception);
            return null;
        }
    }

    private FileDatabase createFileDatabaseConnection(String dataFilePath) throws DataFileCreationException {
        File dataFile = new File(this.configurationDirectory, dataFilePath);
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException exception) {
                throw new DataFileCreationException("The datafile in filelocation %s couldn't be created.".formatted(dataFile.getAbsolutePath()), exception);
            }
        }
        FileDatabase fileDatabase = FileDatabase.fromFile(dataFile);
        this.injector.injectMembers(fileDatabase);
        return fileDatabase;
    }

}
