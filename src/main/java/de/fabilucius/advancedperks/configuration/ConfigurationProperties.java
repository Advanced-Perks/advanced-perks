package de.fabilucius.advancedperks.configuration;

import java.io.File;

public record ConfigurationProperties(String configurationPath, File dataFolder) {
}
