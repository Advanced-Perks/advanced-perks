package de.fabilucius.advancedperks.testdata;

import de.fabilucius.advancedperks.perk.AbstractPerk;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mockito.Mockito;

import java.util.Map;
import java.util.stream.IntStream;

public class PerkTestDataGeneration {

    public Perk aPerkWithFlags(Map<String, Object> flags) {
        return new AbstractPerk(
                FakerInstance.FAKER.text().text(5, 15),
                FakerInstance.FAKER.text().text(5, 15),
                this.aPerkDescription(),
                this.aPerkGuiIcon(),
                true,
                flags
        ) {
        };
    }

    public PerkDescription aPerkDescription() {
        return new PerkDescription(
                IntStream.range(0, FakerInstance.FAKER.number().numberBetween(1, 5))
                        .mapToObj(value -> FakerInstance.FAKER.text().text(15, 20))
                        .toList()
        );
    }

    public PerkGuiIcon aPerkGuiIcon() {
        PerkGuiIcon perkGuiIcon = Mockito.mock(PerkGuiIcon.class);
        Mockito.when(perkGuiIcon.getIcon()).thenReturn(new ItemStack(Material.DIRT));
        return perkGuiIcon;
    }

}
