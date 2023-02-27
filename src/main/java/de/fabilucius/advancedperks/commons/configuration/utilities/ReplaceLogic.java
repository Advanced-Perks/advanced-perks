package de.fabilucius.advancedperks.commons.configuration.utilities;

public class ReplaceLogic {

    private final String replaceKey;
    private final String replaceContent;

    public ReplaceLogic(String replaceKey, String replaceContent) {
        this.replaceKey = replaceKey;
        this.replaceContent = replaceContent;
    }

    /* The getter and setter of this class */

    public String getReplaceKey() {
        return replaceKey;
    }

    public String getReplaceContent() {
        return replaceContent;
    }
}
