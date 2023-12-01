package de.fabilucius.advancedperks.perk.properties;

import java.util.Iterator;
import java.util.List;

public record PerkDescription(List<String> lines) implements Iterable<String> {

    @Override
    public Iterator<String> iterator() {
        return lines.iterator();
    }
}
