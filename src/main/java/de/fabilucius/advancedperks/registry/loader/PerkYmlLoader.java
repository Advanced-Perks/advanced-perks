package de.fabilucius.advancedperks.registry.loader;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.perk.PerksConfiguration;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.registry.loader.exception.PerkClassInstantiationException;
import de.fabilucius.advancedperks.registry.loader.exception.PerkIdentifierAnnotationMissingException;
import de.fabilucius.advancedperks.registry.loader.exception.PerkLoaderException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class PerkYmlLoader implements PerkLoader<PerksConfiguration, PerkLoaderException> {

    @Inject
    private Injector injector;

    @Override
    public <T extends Perk> T loadPerk(Class<T> perkClass, PerksConfiguration from) throws PerkLoaderException {
        PerkIdentifier perkIdentifier = perkClass.getAnnotation(PerkIdentifier.class);
        if (perkIdentifier == null) {
            throw new PerkIdentifierAnnotationMissingException("Unable to load perk class %s, the class is missing the @PerkIdentifier annotation.".formatted(perkClass.getName()));
        }
        String identifier = perkIdentifier.value();
        String displayName = from.getDisplayName(identifier);
        PerkDescription description = from.getPerkDescription(identifier);
        PerkGuiIcon perkGuiIcon = from.getPerkGuiIcon(identifier);
        boolean enabled = from.isEnabled(identifier);
        Map<String, Object> flags = from.getFlags(identifier);
        try {
            T perk = perkClass.getConstructor(String.class, String.class, PerkDescription.class, PerkGuiIcon.class, boolean.class, Map.class)
                    .newInstance(identifier, displayName, description, perkGuiIcon, enabled, flags);
            this.injector.injectMembers(perk);
            return perk;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException exception) {
            throw new PerkClassInstantiationException("Unable to instantiate the perk class %s.".formatted(perkClass.getName()), exception);
        }
    }
}
