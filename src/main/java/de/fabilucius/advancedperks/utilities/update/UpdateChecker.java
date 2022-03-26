package de.fabilucius.advancedperks.utilities.update;

import com.google.gson.Gson;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.Singleton;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class UpdateChecker implements Listener {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private UpdateData updateData;

    private UpdateChecker() {
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
        try {
            URL url = new URL("https://pastebin.com/raw/cCQqpzA3");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (builder.length() > 0) {
                    builder.append(System.lineSeparator());
                }
                builder.append(line);
            }
            reader.close();
            this.updateData = new Gson().fromJson(builder.toString(), UpdateData.class);
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Couldn't check for updates: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if ((player.isOp() || player.hasPermission("advancedperks.admin")) && this.isUpdateAvailable()) {
            this.getUpdateData().ifPresent(data -> {
                player.sendMessage(AdvancedPerks.getMessageConfiguration().getPrefix() + " §7theres an §aupdate §7available for §aAdvanced Perks");
                TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText("§8To see whats new hover over this message."));
                String updateText = data.getChanges().stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.joining("\n"));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(updateText)));
                player.spigot().sendMessage(textComponent);
            });
        }
    }

    /* The getter and setter of this class */

    public Optional<UpdateData> getUpdateData() {
        return this.updateData == null ? Optional.empty() : Optional.of(this.updateData);
    }

    public boolean isUpdateAvailable() {
        return this.getUpdateData().isPresent() &&
                !AdvancedPerks.getInstance().getDescription().getVersion().equalsIgnoreCase(this.getUpdateData().get().getVersion());
    }

    public static class UpdateData {

        private final String version;
        private final List<String> changes;

        public UpdateData(String version, List<String> changes) {
            this.version = version;
            this.changes = changes;
        }

        /* The getter and setter of this class */

        public String getVersion() {
            return version;
        }

        public List<String> getChanges() {
            return changes;
        }
    }

    /* Singleton stuff */

    private static UpdateChecker instance;

    public static UpdateChecker getSingleton() {
        if (instance == null) {
            instance = new UpdateChecker();
        }
        return instance;
    }

}
