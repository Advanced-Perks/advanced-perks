package de.fabilucius.advancedperks.structural;

import com.google.inject.Inject;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import de.fabilucius.advancedperks.AbstractTest;

public abstract class AbstractStructureTest extends AbstractTest {

    protected final JavaClasses javaClasses = new ClassFileImporter().importPackages("de.fabilucius.advancedperks");

    protected ArchCondition<JavaClass> constructorAnnotationCheck(Class<?> annotationClass) {
        return new ArchCondition<>("have a constructor annotated with %s.".formatted(annotationClass.getName())) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                if (item.getConstructors().stream().anyMatch(javaConstructor -> !javaConstructor.isAnnotatedWith(Inject.class))) {
                    events.add(new SimpleConditionEvent(item, false, "%s is missing the annotation %s".formatted(item.getName(), annotationClass.getName())));
                }
            }
        };
    }

}
