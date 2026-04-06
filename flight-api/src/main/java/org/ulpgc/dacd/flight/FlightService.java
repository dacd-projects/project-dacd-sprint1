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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode states = root.get("states");

            if (states != null && states.isArray()) {

                for (JsonNode state : states) {

                    if (state != null && state.size() > 7) {

                        String icao = state.get(0).asText();
                        String callsign = state.get(1).asText();
                        String country = state.get(2).asText();
                        long lastUpdate = state.get(4).asLong();
                        double altitude = state.get(7).asDouble();

                        repository.saveFlight(
                                icao,
                                callsign,
                                country,
                                altitude,
                                lastUpdate
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