package de.fabilucius.advancedperks.utilities.update;

import java.util.List;

public class UpdateData {

    private final String version;
    private final List<String> changes;

    public UpdateData(String version, List<String> changes) {
        this.version = version;
        this.changes = changes;
    }

    /* The getter and setter of this class */

    public String getVersion() {
        return version;
    }

    public List<String> getChanges() {
        return changes;
    }

}
