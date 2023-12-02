package de.fabilucius.advancedperks;

import com.google.inject.Guice;

public abstract class AbstractTest {

    public AbstractTest() {
        Guice.createInjector(new TestModule()).injectMembers(this);
    }

}
