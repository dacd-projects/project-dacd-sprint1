package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.SpaceWeather;
import java.util.List;

public interface NasaFeeder {
    List<SpaceWeather> fetchEvents();
}