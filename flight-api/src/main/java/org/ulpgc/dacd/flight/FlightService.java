package org.ulpgc.dacd.flight;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ulpgc.dacd.persistence.FlightRepository;

public class FlightService {

    private final FlightClient client = new FlightClient();
    private final FlightRepository repository = new FlightRepository();

    public void execute() {

        try {

            String json = client.fetchFlights();

            long capturedAt = System.currentTimeMillis();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode states = root.get("states");

            if (states != null && states.isArray()) {

                for (JsonNode state : states) {

                    if (state != null && state.size() > 9) {

                        String icao = state.get(0).asText();
                        String callsign = state.get(1).asText();
                        String country = state.get(2).asText();

                        long lastUpdate = state.get(4).asLong();

                        double longitude = state.get(5).asDouble();
                        double latitude = state.get(6).asDouble();
                        double altitude = state.get(7).asDouble();
                        double velocity = state.get(9).asDouble();

                        repository.saveFlight(
                                icao,
                                callsign,
                                country,
                                latitude,
                                longitude,
                                altitude,
                                velocity,
                                lastUpdate,
                                capturedAt
                        );
                    }
                }
            }

            System.out.println("Vuelos guardados correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}