package de.fabilucius.advancedperks.data;

import com.google.common.collect.Sets;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PerkData {

    private final UUID uuid;
    private final Set<Perk> enabledPerks;
    private final Set<String> boughtPerks;
    private byte[] dataHash;
    private boolean loaded;

    public PerkData(UUID uuid, Set<Perk> enabledPerks, Set<String> boughtPerks) {
        this.uuid = uuid;
        this.enabledPerks = enabledPerks;
        this.boughtPerks = boughtPerks;
        this.dataHash = this.calculateDataHash();
        this.loaded = false;
    }

    public PerkData(UUID uuid) {
        this(uuid, Sets.newHashSet(), Sets.newHashSet());
    }

    public byte[] calculateDataHash() {
        StringBuilder input = new StringBuilder();
        this.enabledPerks.stream().map(Perk::getIdentifier).forEach(input::append);
        this.boughtPerks.forEach(input::append);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return messageDigest.digest(input.toString().getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<Perk> getEnabledPerks() {
        return enabledPerks;
    }

    public Set<String> getBoughtPerks() {
        return boughtPerks;
    }

    public byte[] getDataHash() {
        return dataHash;
    }

    public void setDataHash(byte[] dataHash) {
        this.dataHash = dataHash;
        this.setLoaded();
    }


    public void setLoaded() {
        this.loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(this.getUuid()));
    }

}

