package de.fabilucius.advancedperks.testdata;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class TestDataGeneration {

    @Inject
    Injector injector;

    public PerkTestDataGeneration perk() {
        return this.injector.getInstance(PerkTestDataGeneration.class);
    }

}
