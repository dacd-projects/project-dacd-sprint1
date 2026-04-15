package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.SpaceWeather;

public interface SpaceWeatherStore {
    void save(SpaceWeather spaceWeather);
}