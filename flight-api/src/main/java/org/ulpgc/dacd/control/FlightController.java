package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Flight;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FlightController {

    private final FlightFeeder feeder;
    private final FlightStore store;

    public FlightController(FlightFeeder feeder, FlightStore store) {
        this.feeder = feeder;
        this.store = store;
    }

    public void run() {

        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(
                this::run,
                0,
                1,
                TimeUnit.HOURS
        );
    }

    private void execute() {
        List<Flight> flights = feeder.fetchFlights();
        for (Flight flight : flights) {
            store.save(flight);
        }
    }
}
