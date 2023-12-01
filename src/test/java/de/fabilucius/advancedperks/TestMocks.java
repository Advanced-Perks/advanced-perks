package de.fabilucius.advancedperks;

import org.bukkit.Server;
import org.mockito.Mockito;

import java.util.logging.Logger;

public class TestMocks {

    public static AdvancedPerks getAdvancedPerksMock() {
        AdvancedPerks advancedPerksMock = Mockito.mock(AdvancedPerks.class);
        Server serverMock = getServerMock();

        Mockito.when(advancedPerksMock.getServer()).thenReturn(serverMock);
        return advancedPerksMock;
    }

    public static Server getServerMock() {
        Server mock = Mockito.mock(Server.class);

        Mockito.when(mock.getLogger()).thenReturn(Logger.getLogger("test-logger"));
        return mock;
    }

}
