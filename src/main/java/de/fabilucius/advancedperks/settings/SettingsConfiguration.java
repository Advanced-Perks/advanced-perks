package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.commons.configuration.Configuration;
import de.fabilucius.advancedperks.commons.sql.SqlType;

public class SettingsConfiguration extends Configuration {
    public SettingsConfiguration() {
        super("settings");
    }

    public final boolean isMetricsEnabled() {
        return this.getValueWithDefault("Global.Metrics-Enabled", true, Boolean.class);
    }

    public final int getGlobalMaxPerks() {
        return this.getValueWithDefault("Global.Max-Perks-Enabled", -1, Integer.class);
    }

    public final SqlType getSqlType() {
        return SqlType.getSqlTypeByName(this.getValueWithDefault("Perk-Data-Save.Save-Method",
                "FILE", String.class));
    }

    public final String getSqlUrl() {
        return this.getValueWithDefault("Perk-Data-Save.Sql-Url",
                "jdbc:mysql://127.0.0.1:3306/perks?useSSL=false", String.class);
    }

    public final String getSqlUserName() {
        return this.getValueWithDefault("Perk-Data-Save.Sql-UserName", "", String.class);
    }

    public final String getSqlPassword() {
        return this.getValueWithDefault("Perk-Data-Save.Sql-Password", "", String.class);
    }

}
