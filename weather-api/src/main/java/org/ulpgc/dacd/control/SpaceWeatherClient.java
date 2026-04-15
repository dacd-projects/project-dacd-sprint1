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
            "https://api.nasa.gov/DONKI/GST?api_key=DEMO_KEY";

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

                String eventType = event.get("gstID").getAsString();

                double kpIndex = 0;
                JsonArray kpArray = event.getAsJsonArray("allKpIndex");
                if (kpArray != null && kpArray.size() > 0) {
                    kpIndex = kpArray.get(0).getAsJsonObject()
                            .get("kpIndex").getAsDouble();
                }

                String startTime = event.get("startTime").getAsString();
                String endTime = event.has("endTime") && !event.get("endTime").isJsonNull()
                        ? event.get("endTime").getAsString()
                        : "";

                events.add(new SpaceWeather(
                        eventType, kpIndex, startTime, endTime, "NASA", capturedAt
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
}