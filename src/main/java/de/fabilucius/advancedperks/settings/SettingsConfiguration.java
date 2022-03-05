package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.sympel.configuration.AbstractConfig;
import de.fabilucius.sympel.configuration.value.types.SingleValue;

public class SettingsConfiguration extends AbstractConfig {
    public SettingsConfiguration() {
        super(AdvancedPerks.getInstance(), "settings.yml");
    }

    public final SingleValue<Boolean> METRICS_ENABLED = new SingleValue<>(this, "Global.Metrics-Enabled", "AWDa whda ", Boolean.class, true);

    public final SingleValue<Integer>
            GLOBAL_MAX_PERKS = new SingleValue<>(this, "Global.Max-Perks-Enabled", "aw ndja wjdak wjdn", Integer.class, -1);

    public final SqlType SQL_TYPE = SqlType.getSqlTypeByName(new SingleValue<>(this, "Perk-Data-Save.Save-Method", " awda widj iawdi wja", String.class, "FILE").get());

    public final SingleValue<String> SQL_URL = new SingleValue<>(this, "Perk-Data-Save.Sql-Url", " wauidhaiu whduia hwdiua hwiduh", String.class, "jdbc:mysql://127.0.0.1:3306/perks?useSSL=false");

    public final SingleValue<String> SQL_USERNAME = new SingleValue<>(this, "Perk-Data-Save.Sql-UserName", "a wdja wodjawoidjoa wjdoi", String.class, "");

    public final SingleValue<String> SQL_PASSWORD = new SingleValue<>(this, "Perk-Data-Save.Sql-Password", "d AWDnajkw ndkj anwdkja nwkjda", String.class, "");

}
