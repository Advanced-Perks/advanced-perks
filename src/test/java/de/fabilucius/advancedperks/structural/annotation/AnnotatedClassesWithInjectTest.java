package de.fabilucius.advancedperks.structural.annotation;

import com.google.inject.Inject;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.fabilucius.advancedperks.compatabilities.AbstractPerkCompatability;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.structural.AbstractStructureTest;
import org.junit.jupiter.api.Test;

class AnnotatedClassesWithInjectTest extends AbstractStructureTest {

    @Test
    void checkCommandClasses() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractCommand.class)
                .should(this.constructorAnnotationCheck(Inject.class))
                .check(this.javaClasses);
    }

    @Test
    void checkSubCommandClasses() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractSubCommand.class)
                .should(this.constructorAnnotationCheck(Inject.class))
                .check(this.javaClasses);
    }

    @Test
    void checkCompatability() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractPerkCompatability.class)
                .should(this.constructorAnnotationCheck(Inject.class))
                .check(this.javaClasses);
    }

}
