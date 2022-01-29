package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.sympel.configuration.types.PluginConfiguration;

public class SettingsConfiguration extends PluginConfiguration {
    public SettingsConfiguration() {
        super(AdvancedPerks.getInstance(), "settings.yml");
    }

    public final boolean isMetricsEnabled() {
        return this.returnFrom("Global.Metrics-Enabled")
                .getAsWithDefault(true, Boolean.class);
    }

    public final int getGlobalMaxPerks() {
        return this.returnFrom("Global.Max-Perks-Enabled")
                .getAsWithDefault(-1, Integer.class);
    }

    public final SqlType getSqlType() {
        return SqlType.getSqlTypeByName(this.returnFrom("Perk-Data-Save.Save-Method")
                .getAsWithDefault("FILE", String.class));
    }

    public final String getSqlUrl() {
        return this.returnFrom("Perk-Data-Save.Sql-Url")
                .getAsWithDefault("jdbc:mysql://127.0.0.1:3306/perks?useSSL=false", String.class);
    }

    public final String getSqlUserName() {
        return this.returnFrom("Perk-Data-Save.Sql-UserName")
                .getAsWithDefault("", String.class);
    }

    public final String getSqlPassword() {
        return this.returnFrom("Perk-Data-Save.Sql-Password")
                .getAsWithDefault("", String.class);
    }

}
