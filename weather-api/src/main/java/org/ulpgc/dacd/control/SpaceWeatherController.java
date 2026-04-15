package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.SpaceWeather;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpaceWeatherController {

    private final NasaFeeder feeder;
    private final SpaceWeatherStore store;

    public SpaceWeatherController(NasaFeeder feeder, SpaceWeatherStore store) {
        this.feeder = feeder;
        this.store = store;
    }

    public void run() {
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                this::execute,
                0,
                1,
                TimeUnit.HOURS
        );
    }

    private void execute() {
        List<SpaceWeather> events = feeder.fetchEvents();
        for (SpaceWeather event : events) {
            store.save(event);
        }
    }
}
