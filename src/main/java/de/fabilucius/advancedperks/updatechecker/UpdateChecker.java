package de.fabilucius.advancedperks.updatechecker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.updatechecker.data.UpdateData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

@Singleton
public class UpdateChecker implements Listener {

    private static final String UPDATE_CHECK_URL = "https://api.spiget.org/v2/resources/96171/versions/latest";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    private UpdateData updateData;
    private boolean updateAvailable;

    @Inject
    public UpdateChecker(AdvancedPerks advancedPerks, APLogger logger) {
        Bukkit.getPluginManager().registerEvents(this, advancedPerks);
        try {
            URI uri = URI.create(UPDATE_CHECK_URL);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                this.updateData = GSON.fromJson(response.body(), UpdateData.class);
                this.updateAvailable = !advancedPerks.getDescription().getVersion().equals(this.updateData.getName());
            }
        } catch (Exception exception) {
            logger.warning("Unable to retrieve update data from spigot to check for new updates: %s".formatted(exception.getMessage()));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if ((player.isOp() || player.hasPermission("advancedperks.admin")) && this.updateAvailable) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GRAY + "There is an update available for " + ChatColor.GOLD + "Advanced Perks " + ChatColor.GRAY + " the latest version is " + ChatColor.GOLD + this.updateData.getName());
            player.sendMessage(ChatColor.GRAY + "The update was released " + ChatColor.GOLD + this.updateData.getReleaseDate().toString());
            player.sendMessage(" ");
        }
    }

}
