package de.fabilucius.advancedperks.commons.database.details;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class Credentials {

    private final String userName;
    private final String password;

    protected Credentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public static Credentials withoutAuth() {
        return new Credentials("", "");
    }

    public static Credentials withAuth(String userName, String password) {
        Preconditions.checkState(!Strings.isNullOrEmpty(userName), "userName in auth cannot be null or empty");
        Preconditions.checkState(!Strings.isNullOrEmpty(password), "password in auth cannot be null or empty");
        return new Credentials(userName, password);
    }

    /* the getter and setter of the class */

    public boolean isAuthEmpty() {
        return this.getUserName().isEmpty() && this.getPassword().isEmpty();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
