package de.fabilucius.advancedperks.datastructure;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AbstractTest;
import de.fabilucius.advancedperks.core.datastructure.DualKeyMap;
import de.fabilucius.advancedperks.core.datastructure.DualKeyMaps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DualKeyMapTest extends AbstractTest {

    @Test
    void testRemoveLogicFirstKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertEquals("testValue", dualKeyMap.removeByFirstKey("testKey"));

        Assertions.assertNull(dualKeyMap.getByFirstKey("testKey"));
    }

    @Test
    void testRemoveLogicSecondKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertEquals("testValue", dualKeyMap.removeBySecondKey(1337));

        Assertions.assertNull(dualKeyMap.getBySecondKey(1337));
    }

    @Test
    void testGetByFirstKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertEquals("testValue", dualKeyMap.getByFirstKey("testKey"));
    }

    @Test
    void testGetBySecondKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertEquals("testValue", dualKeyMap.getBySecondKey(1337));
    }

    @Test
    void testContainsFirstKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertTrue(dualKeyMap.containsFirstKey("testKey"));
    }

    @Test
    void testContainsSecondKey() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");

        Assertions.assertTrue(dualKeyMap.containsSecondKey(1337));
    }

    @Test
    void testValuesLogic() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");
        dualKeyMap.putValue("testSecondKey", 420, "testValue2");

        List<String> expected = Lists.newArrayList("testValue", "testValue2").stream().sorted().toList();
        List<String> actual = dualKeyMap.values().stream().sorted().toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFirstKeyOverwriting() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");
        dualKeyMap.putValue("testKey", 23, "testValue2");

        Assertions.assertEquals("testValue2", dualKeyMap.getByFirstKey("testKey"));
        Assertions.assertEquals("testValue2", dualKeyMap.getBySecondKey(23));
        Assertions.assertNull(dualKeyMap.getBySecondKey(1337));
        Assertions.assertEquals(1, dualKeyMap.size());
    }

    @Test
    void testSecondKeyOverwriting() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");
        dualKeyMap.putValue("otherTestKey", 1337, "testValue2");

        Assertions.assertEquals("testValue2", dualKeyMap.getByFirstKey("otherTestKey"));
        Assertions.assertEquals("testValue2", dualKeyMap.getBySecondKey(1337));
        Assertions.assertNull(dualKeyMap.getByFirstKey("testKey"));
        Assertions.assertEquals(1, dualKeyMap.size());
    }

    @Test
    void testDualKeyMapSize() {
        DualKeyMap<String, Integer, String> dualKeyMap = DualKeyMaps.newDualKeyMap();
        dualKeyMap.putValue("testKey", 1337, "testValue");
        dualKeyMap.putValue("otherTestKey", 123, "sheesh");
        dualKeyMap.putValue("evenOtherTestKey", 1552, "sheesh");
        dualKeyMap.putValue("otherTestKey", 32353, "sheesh");

        Assertions.assertEquals(3, dualKeyMap.size());
    }

}
