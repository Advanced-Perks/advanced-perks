package de.fabilucius.advancedperks;

import com.google.inject.Guice;

public class AbstractTest {

    public AbstractTest() {
        Guice.createInjector(new TestModule()).injectMembers(this);
    }

}
