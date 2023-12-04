package de.fabilucius.advancedperks.core.mojang;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public final class MojangProfileFetcher {

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final JsonParser JSON_PARSER = new JsonParser();

    private MojangProfileFetcher() {
    }

    public static MojangProfileData fetchPlayerProfile(String name) {
        try {
            URI uri = URI.create(MOJANG_API_URL + name);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject jsonObject = JSON_PARSER.parse(response.body()).getAsJsonObject();
                return new MojangProfileData(
                        jsonObject.get("name").getAsString(),
                        UUID.fromString(jsonObject.get("id").getAsString()));
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

}
