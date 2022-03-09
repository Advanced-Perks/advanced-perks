package de.fabilucius.advancedperks.data;

import com.google.common.collect.Sets;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.perks.Perk;
import org.bukkit.entity.Player;

import java.util.OptionalInt;
import java.util.Set;
import java.util.regex.Pattern;

public class PerkData {
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("advancedperks.maxperks.\\d+\\b");

    private final Player player;
    private int maxPerks;
    private final Set<Perk> activatedPerks = Sets.newHashSet();

    public PerkData(Player player) {
        this.player = player;
        this.maxPerks = this.queryMaxPerks();
        AdvancedPerks.getPerkStateController().loadPerkData(this);
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
