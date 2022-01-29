package de.fabilucius.advancedperks.commons;

import org.bukkit.Bukkit;

import java.util.Arrays;

public enum ServerVersion {

    v1_8(0, "1.8"),
    v1_9(1, "1.9"),
    v1_10(2, "1.10"),
    v1_11(3, "1.11"),
    v1_12(4, "1.12"),
    v1_13(5, "1.13"),
    v1_14(6, "1.14"),
    v1_15(7, "1.15"),
    v1_16(8, "1.16"),
    v1_17(9, "1.17"),
    UNKNOWN(10, "UNKNOWN");

    ServerVersion(int order, String version) {
        this.order = order;
        this.version = version;
    }

    public static final ServerVersion SERVER_VERSION = fetchServerVersion();
    private final int order;
    private final String version;

    public static ServerVersion fetchServerVersion() {
        String[] versionParts = Bukkit.getBukkitVersion().split("-");
        if (versionParts.length >= 1) {
            return Arrays.stream(values()).filter(serverVersion -> versionParts[0]
                    .startsWith(serverVersion.getVersion())).findFirst().orElse(UNKNOWN);
        }
        return UNKNOWN;
    }

    public static boolean isServerVersionHigherOrEqual(ServerVersion serverVersion) {
        return SERVER_VERSION.getOrder() >= serverVersion.getOrder();
    }

    /* the getter and setter of this class */

    public int getOrder() {
        return order;
    }

    public String getVersion() {
        return version;
    }
}
