package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Flight;
import java.util.List;

public class FlightController {

    private final FlightFeeder feeder;
    private final FlightStore store;

    public FlightController(FlightFeeder feeder, FlightStore store) {
        this.feeder = feeder;
        this.store = store;
    }

    public void execute() {
        List<Flight> flights = feeder.fetchFlights();
        for (Flight flight : flights) {
            store.save(flight);
        }
    }
}
