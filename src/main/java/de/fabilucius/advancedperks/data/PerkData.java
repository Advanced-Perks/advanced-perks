package de.fabilucius.advancedperks.data;

import com.google.common.collect.Sets;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.perks.tasks.LoadPerkDataTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.OptionalInt;
import java.util.Set;
import java.util.regex.Pattern;

public class PerkData {
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("advancedperks.maxperks.\\d+\\b");

    private final Player player;
    private int maxPerks;
    private final Set<Perk> activatedPerks = Sets.newHashSet();
    private final Set<String> unlockedPerks = Sets.newHashSet();

    public PerkData(Player player) {
        this.player = player;
        this.maxPerks = this.queryMaxPerks();
        Bukkit.getScheduler().runTaskLaterAsynchronously(AdvancedPerks.getInstance(), new LoadPerkDataTask(this), 40L);
    }

    private int queryMaxPerks() {
        OptionalInt possibleMaxPerks = player.getEffectivePermissions().stream().filter(permissionAttachmentInfo -> PERMISSION_PATTERN.matcher(permissionAttachmentInfo.getPermission()).matches()).mapToInt(value -> {
            String potentialAmount = value.getPermission().replaceAll("advancedperks.maxperks.", "");
            try {
                return Integer.parseInt(potentialAmount);
            } catch (Exception ignored) {
                return 0;
            }
        }).max();
        return possibleMaxPerks.isPresent() ? possibleMaxPerks.getAsInt() : -1;
    }

    public boolean isPerkUnlocked(Perk perk) {
        return this.getUnlockedPerks().contains(perk.getIdentifier());
    }

    public boolean isPerkActivated(Perk perk) {
        return this.getActivatedPerks().contains(perk);
    }

    public int getAmountOfActivatedPerks() {
        return this.getActivatedPerks().size();
    }

    public void refreshMaxPerks() {
        this.setMaxPerks(this.queryMaxPerks());
    }

    /* the getter and setter of this class */

    public int getMaxPerks() {
        return maxPerks;
    }

    public Set<String> getUnlockedPerks() {
        return unlockedPerks;
    }

    public void setMaxPerks(int maxPerks) {
        this.maxPerks = maxPerks;
    }

    public Set<Perk> getActivatedPerks() {
        return activatedPerks;
    }

    public Player getPlayer() {
        return player;
    }
}
