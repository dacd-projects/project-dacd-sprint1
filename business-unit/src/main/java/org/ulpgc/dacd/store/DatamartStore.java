package org.ulpgc.dacd.store;

import org.ulpgc.dacd.model.Flight;
import org.ulpgc.dacd.model.SpaceWeather;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatamartStore {

    private final Map<String, Flight> activeFlights = new ConcurrentHashMap<>();
    private SpaceWeather latestSpaceWeather;

    public void updateFlight(Flight flight) {
        activeFlights.put(flight.icao(), flight);
    }

    public void updateSpaceWeather(SpaceWeather spaceWeather) {
        this.latestSpaceWeather = spaceWeather;
    }

    public Collection<Flight> getActiveFlights() {
        return activeFlights.values();
    }

    public SpaceWeather getLatestSpaceWeather() {
        return latestSpaceWeather;
    }
}