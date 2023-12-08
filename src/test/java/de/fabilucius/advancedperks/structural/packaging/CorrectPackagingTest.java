package de.fabilucius.advancedperks.structural.packaging;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.fabilucius.advancedperks.api.placeholderapi.AbstractAdvancedPerksExpansion;
import de.fabilucius.advancedperks.compatabilities.AbstractPerkCompatability;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.defaultelements.AbstractDefaultGuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.AbstractGuiWindow;
import de.fabilucius.advancedperks.core.guisystem.window.types.AbstractPageGuiWindow;
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

    @Test
    void testGuiWindowPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractGuiWindow.class)
                .and()
                .areNotAssignableFrom(AbstractGuiWindow.class)
                .and()
                .areNotAssignableFrom(AbstractPageGuiWindow.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.guisystem..")
                .check(this.javaClasses);
    }

    @Test
    void testGuiElementPackaging() {
        ArchRuleDefinition.classes().that().areAssignableTo(AbstractGuiElement.class)
                .and()
                .areNotAssignableFrom(AbstractGuiElement.class)
                .and()
                .areNotAssignableTo(AbstractDefaultGuiElement.class)
                .should()
                .resideInAPackage("de.fabilucius.advancedperks.guisystem..elements..")
                .check(this.javaClasses);
    }

}
