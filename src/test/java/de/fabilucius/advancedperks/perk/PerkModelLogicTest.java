package de.fabilucius.advancedperks.perk;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AbstractTest;
import de.fabilucius.advancedperks.testdata.perk.TestEffectPerk;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

class PerkModelLogicTest extends AbstractTest {

    @Mock
    Player player;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(player.addPotionEffects(Mockito.anyList())).thenReturn(true);
    }

    @Test
    void testEffectPerkEnableLogic() {
        List<PotionEffect> potionEffects = Lists.newArrayList(
                new PotionEffect(PotionEffectType.GLOWING, 50, 50, false, false),
                new PotionEffect(PotionEffectType.JUMP, 50, 50, false, false)
        );
        TestEffectPerk testEffectPerk = new TestEffectPerk(potionEffects);
        testEffectPerk.onPrePerkEnable(this.player);
        Mockito.verify(this.player).addPotionEffects(potionEffects);
        Mockito.verify(this.player, Mockito.times(1)).addPotionEffects(potionEffects);
    }

    @Test
    void testEffectPerkDisableLogic() {
        List<PotionEffect> potionEffects = Lists.newArrayList(
                new PotionEffect(PotionEffectType.GLOWING, 50, 50, false, false),
                new PotionEffect(PotionEffectType.JUMP, 50, 50, false, false)
        );
        TestEffectPerk testEffectPerk = new TestEffectPerk(potionEffects);
        testEffectPerk.onPrePerkDisable(this.player);
        Mockito.verify(this.player).removePotionEffect(potionEffects.get(0).getType());
        Mockito.verify(this.player).removePotionEffect(potionEffects.get(1).getType());
        Mockito.verify(this.player, Mockito.times(2)).removePotionEffect(Mockito.any());
    }

}