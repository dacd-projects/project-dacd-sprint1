package org.ulpgc.dacd.control;

import org.junit.jupiter.api.Test;
import org.ulpgc.dacd.model.SpaceWeather;

import java.util.ArrayList;
import java.util.List;

public class SpaceWeatherControllerTest {

    @Test
    void controllerCallsStoreForEachEvent() {

        NasaFeeder fakeFeeder = () -> List.of(
                new SpaceWeather("GST-001", 6.5,
                        "2024-04-01T12:00Z", "2024-04-01T18:00Z",
                        "NASA", 1713000001L),
                new SpaceWeather("GST-002", 4.0,
                        "2024-04-02T08:00Z", "2024-04-02T14:00Z",
                        "NASA", 1713000002L)
        );

        List<SpaceWeather> saved = new ArrayList<>();
        SpaceWeatherStore fakeStore = saved::add;

        SpaceWeatherController controller = new SpaceWeatherController(fakeFeeder, fakeStore);
        controller.execute();

        assert saved.size() == 2;
    }
}