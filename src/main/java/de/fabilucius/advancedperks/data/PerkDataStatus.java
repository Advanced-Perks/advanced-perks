package de.fabilucius.advancedperks.data;

public class PerkDataStatus {

    /* Whether the perk data was loaded from its datasource */
    private boolean dataLoaded;
    /* Whether the perk data has changed since it was loaded */
    private boolean dataChanged;

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded() {
        this.dataLoaded = true;
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged() {
        if (this.dataLoaded) {
            this.dataChanged = true;
        }
    }
}
