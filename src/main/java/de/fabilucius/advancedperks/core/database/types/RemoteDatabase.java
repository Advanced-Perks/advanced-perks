package de.fabilucius.advancedperks.core.database.types;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import de.fabilucius.advancedperks.core.database.AbstractDatabase;
import de.fabilucius.advancedperks.core.database.Credentials;

public class RemoteDatabase extends AbstractDatabase {
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

}
