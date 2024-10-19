package de.fabilucius.advancedperks;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.testdata.TestDataGeneration;

import java.io.File;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AdvancedPerks.class).toInstance(TestMocks.getAdvancedPerksMock());
        bind(APLogger.class).asEagerSingleton();
        bind(File.class).annotatedWith(Names.named("configurationDirectory")).toInstance(new File("src/test/resources/configuration", "temp_configuration_directory"));
        bind(TestDataGeneration.class).asEagerSingleton();
    }
}
