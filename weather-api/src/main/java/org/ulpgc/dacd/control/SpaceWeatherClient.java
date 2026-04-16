package org.ulpgc.dacd.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ulpgc.dacd.model.SpaceWeather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SpaceWeatherClient implements NasaFeeder {

    private static final String URL =
            "https://services.swpc.noaa.gov/json/planetary_k_index_1m.json";

    @Override
    public List<SpaceWeather> fetchEvents() {

        List<SpaceWeather> events = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long capturedAt = System.currentTimeMillis();

            JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();

            for (JsonElement element : array) {

                JsonObject event = element.getAsJsonObject();

                String timeTag = event.get("time_tag").getAsString();
                double kpIndex = event.get("estimated_kp").getAsDouble();

                events.add(new SpaceWeather(
                        "KP_INDEX", kpIndex, timeTag, "", "NOAA", capturedAt
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
}