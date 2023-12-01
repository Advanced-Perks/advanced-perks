package de.fabilucius.advancedperks.core.guisystem;

import org.bukkit.Sound;

public enum GuiSound {

    NORMAL_CLICK(Sound.BLOCK_TRIPWIRE_CLICK_ON, 1, 1),
    ON_CLICK(Sound.BLOCK_TRIPWIRE_CLICK_ON, 1, 1.3F),
    OFF_CLICK(Sound.BLOCK_TRIPWIRE_CLICK_ON, 1, 0.7F),
    ERROR_CLICK(Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.5F),
    SETUP_CLICK(Sound.BLOCK_NOTE_BLOCK_BIT, 1, 2);

    private final Sound sound;
    private final float volume;
    private final float pitch;

    GuiSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
