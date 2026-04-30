package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Flight;
import org.ulpgc.dacd.model.SpaceWeather;
import org.ulpgc.dacd.store.DatamartStore;

public class DatamartUpdater {

    private final DatamartStore datamartStore;
    private final Gson gson = new Gson();

    public DatamartUpdater(DatamartStore datamartStore) {
        this.datamartStore = datamartStore;
    }

    public void process(String json) {
        JsonObject obj = gson.fromJson(json, JsonObject.class);
        String ss = obj.get("ss").getAsString();

        if (ss.equals("flight-api")) {
            Flight flight = gson.fromJson(json, Flight.class);
            datamartStore.updateFlight(flight);
        } else if (ss.equals("weather-api")) {
            SpaceWeather weather = gson.fromJson(json, SpaceWeather.class);
            datamartStore.updateSpaceWeather(weather);
        }
    }
}
