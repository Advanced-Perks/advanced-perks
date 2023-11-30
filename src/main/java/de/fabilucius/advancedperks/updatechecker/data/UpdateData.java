package de.fabilucius.advancedperks.updatechecker.data;

import java.util.Date;
import java.util.UUID;

public record UpdateData(int downloads, String name, Rating rating, Date releaseDate, int resource, UUID uuid, int id) {

}
