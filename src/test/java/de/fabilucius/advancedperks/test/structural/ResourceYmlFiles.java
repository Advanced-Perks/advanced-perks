package de.fabilucius.advancedperks.test.structural;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class ResourceYmlFiles {

    @Test
    public void checkResourceYmlFilesHaveValidFormat() {
        File resourceFolder = new File("src/main/resources");
        File[] ymlFiles = resourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml") || name.toLowerCase().endsWith(".yaml"));
        Assert.assertNotNull(ymlFiles);
        Arrays.stream(ymlFiles).forEach(file -> {
            try {
                new Yaml().load(new FileInputStream(file));
            } catch (YAMLException | FileNotFoundException e) {
                Assert.fail("The file %s has invalid yml formatting or doesn't exist: %s".formatted(file.getName(),e.getMessage()));
            }
        });
    }

    @Test
    public void checkAllYmlFilesExist() {
        File resourceFolder = new File("src/main/resources");
        String[] expectedFiles = {"message.yml", "perks.yml", "plugin.yml", "settings.yml"};
        Arrays.stream(expectedFiles).forEach(file -> {
            if (!new File(resourceFolder, file).exists()) {
                Assert.fail("The file %s does not exist in the resource folder.".formatted(file));
            }
        });
    }

}
