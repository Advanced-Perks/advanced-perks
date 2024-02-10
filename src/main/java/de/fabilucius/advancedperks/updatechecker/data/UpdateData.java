package de.fabilucius.advancedperks.updatechecker.data;

import java.util.Date;
import java.util.UUID;

public class UpdateData {

    private int downloads;
    private String name;
    private Rating rating;
    private Date releaseDate;
    private int resource;
    private UUID uuid;
    private int id;

    public String getName() {
        return name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
}
