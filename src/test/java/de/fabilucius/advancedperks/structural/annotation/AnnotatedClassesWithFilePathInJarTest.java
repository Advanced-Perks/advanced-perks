package de.fabilucius.advancedperks.structural.annotation;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.fabilucius.advancedperks.core.configuration.Configuration;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.core.configuration.types.AbstractMessageConfiguration;
import de.fabilucius.advancedperks.structural.AbstractStructureTest;
import org.junit.jupiter.api.Test;

class AnnotatedClassesWithFilePathInJarTest extends AbstractStructureTest {

    @Test
    void checkConfigurationClasses() {
        ArchRuleDefinition.classes().that().areAssignableTo(Configuration.class)
                .and()
                .areNotAssignableFrom(Configuration.class)
                .and()
                .areNotAssignableFrom(AbstractMessageConfiguration.class)
                .should()
                .beAnnotatedWith(FilePathInJar.class)
                .check(this.javaClasses);
    }

}
