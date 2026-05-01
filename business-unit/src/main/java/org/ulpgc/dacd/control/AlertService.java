package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Flight;
import org.ulpgc.dacd.model.FlightRiskAlert;
import org.ulpgc.dacd.model.SpaceWeather;
import org.ulpgc.dacd.store.DatamartStore;

import java.util.List;

public class AlertService {

    private static final double KP_THRESHOLD = 5.0;
    private static final double POLAR_LATITUDE = 60.0;

    private final DatamartStore datamartStore;

    public AlertService(DatamartStore datamartStore) {
        this.datamartStore = datamartStore;
    }

    public List<FlightRiskAlert> getFlightsAtRisk() {
        SpaceWeather weather = datamartStore.getLatestSpaceWeather();

        if (weather == null || weather.kpIndex() < KP_THRESHOLD) {
            return List.of();
        }

        return datamartStore.getActiveFlights().stream()
                .filter(f -> Math.abs(f.latitude()) >= POLAR_LATITUDE)
                .map(f -> new FlightRiskAlert(
                        f.icao(),
                        f.callsign(),
                        f.latitude(),
                        f.longitude(),
                        f.altitude(),
                        weather.kpIndex(),
                        getRiskLevel(weather.kpIndex())
                ))
                .toList();
    }

    private String getRiskLevel(double kpIndex) {
        if (kpIndex >= 8) return "SEVERE";
        if (kpIndex >= 6) return "HIGH";
        return "MODERATE";
    }
}