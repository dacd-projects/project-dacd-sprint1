package org.ulpgc.dacd.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ulpgc.dacd.model.Flight;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OpenSkyFlightFeeder implements FlightFeeder {

    private static final String URL = "https://opensky-network.org/api/states/all";

    @Override
    public List<Flight> fetchFlights() {

        List<Flight> flights = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long capturedAt = System.currentTimeMillis();

            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray states = root.getAsJsonArray("states");

            if (states == null) return flights;

            for (JsonElement element : states) {

                JsonArray state = element.getAsJsonArray();

                if (state == null || state.size() <= 9) continue;

                String callsign = state.get(1).getAsString().trim();

                if (!isRelevantFlight(callsign)) continue;

                String icao = state.get(0).getAsString();
                String country = state.get(2).getAsString();
                if (state.get(4).isJsonNull() || state.get(5).isJsonNull() ||
                        state.get(6).isJsonNull() || state.get(7).isJsonNull() ||
                        state.get(9).isJsonNull()) continue;

                Instant lastUpdate = Instant.ofEpochSecond(state.get(4).getAsLong());
                double longitude = state.get(5).getAsDouble();
                double latitude = state.get(6).getAsDouble();
                double altitude = state.get(7).getAsDouble();
                double velocity = state.get(9).getAsDouble();

                flights.add(new Flight(
                        icao, callsign, country,
                        latitude, longitude, altitude,
                        velocity, lastUpdate, capturedAt
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flights;
    }

    private boolean isRelevantFlight(String callsign) {
        return callsign != null && !callsign.isEmpty();
    }
}
