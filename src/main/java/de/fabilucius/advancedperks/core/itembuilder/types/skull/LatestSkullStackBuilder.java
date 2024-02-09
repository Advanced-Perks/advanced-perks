package de.fabilucius.advancedperks.core.itembuilder.types.skull;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public final class LatestSkullStackBuilder {

    private static final UUID RANDOM_UUID = UUID.randomUUID();

    private LatestSkullStackBuilder() {
    }

    public static SkullStackBuilder setBase64Value(SkullStackBuilder skullStackBuilder, String base64Value) throws MalformedURLException {
        PlayerProfile playerProfile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures playerTextures = playerProfile.getTextures();
        playerTextures.setSkin(getUrlFromBase64(base64Value));
        playerProfile.setTextures(playerTextures);
        ((SkullMeta) skullStackBuilder.getItemMeta()).setOwnerProfile(playerProfile);
        return skullStackBuilder;
    }

    /* Source: https://blog.jeff-media.com/creating-custom-heads-in-spigot-1-18-1/ */
    private static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

}
