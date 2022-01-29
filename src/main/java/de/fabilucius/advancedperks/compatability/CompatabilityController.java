package de.fabilucius.advancedperks.compatability;

import de.fabilucius.advancedperks.compatability.compats.ChangeWorldCompatability;
import de.fabilucius.advancedperks.compatability.compats.PotionDesyncCompatability;

public class CompatabilityController {

    public CompatabilityController() {
        new PotionDesyncCompatability();
        new ChangeWorldCompatability();
    }

}
