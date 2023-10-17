package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.advancedperks.commons.configuration.AbstractConfig;
import de.fabilucius.advancedperks.commons.configuration.value.types.SingleValue;

public class SettingsConfiguration extends AbstractConfig {
    public SettingsConfiguration() {
        super(AdvancedPerks.getInstance(), "settings.yml");
    }

    public final boolean isMetricsCollectionEnabled() {
        return this.getConfig().getBoolean("Global.Metrics-Enabled", true);
    }

    public final boolean isLuckPermsSupportEnabled() {
        return this.getConfig().getBoolean("Global.Enable-LuckPerms-Support", true);
    }

    public final SingleValue<Boolean> METRICS_ENABLED = new SingleValue<>(this, "Global.Metrics-Enabled", "This option controls if the metrics for the plugin should be enabled or not.", Boolean.class, true);
    public final SingleValue<Integer> GLOBAL_MAX_PERKS = new SingleValue<>(this, "Global.Max-Perks-Enabled", "This value controls the global limit for everyone who doesn't bypass it on how many perks he can enabled at once.", Integer.class, -1);
    public final SqlType SQL_TYPE = SqlType.getSqlTypeByName(new SingleValue<>(this, "Perk-Data-Save.Save-Method", "This value controls the way the perk data gets saved, you can choose between FILE and DATABASE based on what you want.", String.class, "FILE").get());
    public final SingleValue<String> SQL_URL = new SingleValue<>(this, "Perk-Data-Save.Sql-Url", "If the save-method is DATABASE this is the connection string it uses to connect to the database.", String.class, "jdbc:mysql://127.0.0.1:3306/perks?useSSL=false");
    public final SingleValue<String> SQL_USERNAME = new SingleValue<>(this, "Perk-Data-Save.Sql-UserName", "This is the username for the database authentication.", String.class, "");
    public final SingleValue<String> SQL_PASSWORD = new SingleValue<>(this, "Perk-Data-Save.Sql-Password", "This is the password for the database authentication.", String.class, "");

}
