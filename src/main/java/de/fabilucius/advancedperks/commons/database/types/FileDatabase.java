package de.fabilucius.advancedperks.commons.database.types;

import com.google.common.base.Preconditions;
import de.fabilucius.advancedperks.commons.database.AbstractDatabase;
import de.fabilucius.advancedperks.commons.database.details.Credentials;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDatabase extends AbstractDatabase {

    private static final Logger LOGGER = Bukkit.getLogger();

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
                LOGGER.log(Level.SEVERE, "There was an error while creating the file for a database:", ioException);
            }
        }
        return new FileDatabase("jdbc:sqlite:"+file.getPath());
    }

}
