package de.fabilucius.advancedperks.perks.types;

import de.fabilucius.advancedperks.perks.AbstractPerk;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public abstract class AbstractEffectPerk extends AbstractPerk {

    private final PotionEffect potionEffect;

    public AbstractEffectPerk(String identifier, PotionEffect potionEffect) {
        super(identifier);
        this.potionEffect = potionEffect;
    }

    public AbstractEffectPerk(String identifier, String displayName, String permission, List<String> description, PotionEffect potionEffect) {
        super(identifier, displayName, permission, description);
        this.potionEffect = potionEffect;
    }

    @Override
    public void prePerkEnable(Player player) {
        player.addPotionEffect(this.getPotionEffect());
        super.prePerkEnable(player);
    }

    @Override
    public void prePerkDisable(Player player) {
        player.removePotionEffect(this.getPotionEffect().getType());
        super.prePerkDisable(player);
    }

    /* the getter and setter of this class */

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }
}
