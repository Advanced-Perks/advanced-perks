package de.fabilucius.advancedperks.structural.packaging;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.fabilucius.advancedperks.api.placeholderapi.AbstractAdvancedPerksExpansion;
import de.fabilucius.advancedperks.compatabilities.AbstractPerkCompatability;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.event.AdvancedPerksEvent;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.structural.AbstractStructureTest;
import org.junit.jupiter.api.Test;

class CorrectPackagingTest extends AbstractStructureTest {

    @Test
    void testPlaceholderExpansionPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractAdvancedPerksExpansion.class)
                .and()
                .areNotAssignableFrom(AbstractAdvancedPerksExpansion.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.api.placeholderapi..")
                .check(this.javaClasses);
    }

    @Test
    void testDefaultPerksPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractDefaultPerk.class)
                .and()
                .areNotAssignableFrom(AbstractDefaultPerk.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.perk.defaultperks..")
                .check(this.javaClasses);
    }

    @Test
    void testCommandsPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractCommand.class)
                .and()
                .areNotAssignableFrom(AbstractCommand.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.command..")
                .check(this.javaClasses);
    }

    @Test
    void testSubCommandsPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractSubCommand.class)
                .and()
                .areNotAssignableFrom(AbstractSubCommand.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.command..subcommands..")
                .check(this.javaClasses);
    }

    @Test
    void testCompatibilitiesPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractPerkCompatability.class)
                .and()
                .areNotAssignableFrom(AbstractPerkCompatability.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.compatabilities..")
                .check(this.javaClasses);
    }

    @Test
    void testEventPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AdvancedPerksEvent.class)
                .and()
                .areNotAssignableFrom(AdvancedPerksEvent.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.event..")
                .check(this.javaClasses);
    }

}
