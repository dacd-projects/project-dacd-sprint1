package org.ulpgc.dacd.spaceweather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ulpgc.dacd.control.SpaceWeatherClient;
import org.ulpgc.dacd.persistence.SpaceWeatherRepository;

public class SpaceWeatherService {

    private final SpaceWeatherClient client = new SpaceWeatherClient();
    private final SpaceWeatherRepository repository = new SpaceWeatherRepository();

    public void execute() {

        try {

            String json = client.fetchEvents();

            if (json == null || json.isEmpty()) {
                System.out.println("No hay datos de clima espacial");
                return;
            }

            long capturedAt = System.currentTimeMillis();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode array = mapper.readTree(json);

            for (JsonNode event : array) {

                String type = event.get("gstID").asText();

                double kp = 0;

                JsonNode kpArray = event.get("allKpIndex");
                if (kpArray != null && kpArray.isArray() && kpArray.size() > 0) {
                    kp = kpArray.get(0).get("kpIndex").asDouble();
                }

                String start = event.get("startTime").asText();
                String end = event.get("endTime") != null
                        ? event.get("endTime").asText()
                        : "";

                repository.saveEvent(type, kp, start, end, "NASA", capturedAt);


                if (kp >= 5) {
                    System.out.println("⚠️ ALERTA: Tormenta geomagnética detectada (Kp=" + kp + ")");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}