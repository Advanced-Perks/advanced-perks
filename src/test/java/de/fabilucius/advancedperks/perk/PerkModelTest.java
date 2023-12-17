package de.fabilucius.advancedperks.perk;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.AbstractTest;
import de.fabilucius.advancedperks.testdata.TestDataGeneration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

class PerkModelTest extends AbstractTest {

    @Inject
    TestDataGeneration testDataGeneration;

    @Test
    void testPermissionFlagPresent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("permission", "test.permission"));
        Assertions.assertTrue(perk.getPermission().isPresent());
        Assertions.assertEquals("test.permission", perk.getPermission().get());
    }

    @Test
    void testPermissionFlagPresentWithWrongData() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("permission", Lists.newArrayList()));
        Assertions.assertTrue(perk.getPermission().isPresent());
        Assertions.assertNotNull(perk.getPermission().get());
    }

    @Test
    void testPermissionFlagAbsent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(Maps.newHashMap());
        Assertions.assertTrue(perk.getPermission().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> perk.getPermission().get());
    }

    @Test
    void testDisallowedWorldsFlagPresent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("disallowed_worlds", Lists.newArrayList("world1", "world2")));
        Assertions.assertTrue(perk.getDisallowedWorlds().isPresent());
        Assertions.assertEquals("world1", perk.getDisallowedWorlds().get().get(0));
        Assertions.assertEquals("world2", perk.getDisallowedWorlds().get().get(1));
    }

    @Test
    void testDisallowedWorldsFlagPresentWithWrongDataType() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("disallowed_worlds", "wrong datatype"));
        Assertions.assertFalse(perk.getDisallowedWorlds().isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> perk.getDisallowedWorlds().get());
    }

    @Test
    void testDisallowedWorldsFlagAbsent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(Maps.newHashMap());
        Assertions.assertTrue(perk.getDisallowedWorlds().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> perk.getDisallowedWorlds().get());
    }

    @Test
    void testPriceFlagPresent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("price", 25.2D));
        Assertions.assertTrue(perk.getPrice().isPresent());
        Assertions.assertEquals(25.2D, perk.getPrice().get());
    }

    @Test
    void testPriceFlagPresentWithWrongDataType() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(ImmutableMap.of("price", "wrong datatype"));
        Assertions.assertFalse(perk.getPrice().isPresent());
        Assertions.assertThrows(NoSuchElementException.class, () -> perk.getPrice().get());
    }

    @Test
    void testPriceFlagAbsent() {
        Perk perk = this.testDataGeneration.perk().aPerkWithFlags(Maps.newHashMap());
        Assertions.assertTrue(perk.getDisallowedWorlds().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> perk.getPrice().get());
    }

}
