package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AbstractTest;
import de.fabilucius.advancedperks.core.configuration.Configuration;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;
import jakarta.inject.Named;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public abstract class AbstractConfigurationTest extends AbstractTest {

    @Inject
    @Named("configurationDirectory")
    File configurationDirectory;

    @AfterEach
    void cleanupConfigurationDirectory() throws IOException {
        if (this.configurationDirectory.exists()) {
            try (Stream<Path> pathStream = Files.walk(this.configurationDirectory.toPath())) {
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

}

@FilePathInJar("configuration/test_config.yml")
class TestConfiguration extends Configuration {

    public String getTestString() {
        return this.getString("test_string");
    }

}

@FilePathInJar("configuration/not_existing_test_configuration.yml")
class NotExistingTestConfiguration extends Configuration {
}

@FilePathInJar("configuration/invalid_test_config.yml")
class InvalidTestConfiguration extends Configuration {
}
