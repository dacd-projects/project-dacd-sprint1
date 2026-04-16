package org.ulpgc.dacd.control;

import org.junit.jupiter.api.Test;
import org.ulpgc.dacd.model.Flight;

import java.time.Instant;
import java.util.List;

public class FlightControllerTest {

    @Test
    void controllerCallsStoreForEachFlight() {

        FlightFeeder fakeFeeder = () -> List.of(
                new Flight("abc1", "ANC001", "Norway",
                        70.5, 25.3, 10000.0, 250.0,
                        Instant.ofEpochSecond(1713000000L), 1713000001L),
                new Flight("abc2", "KEF002", "Iceland",
                        65.0, -22.0, 9000.0, 230.0,
                        Instant.ofEpochSecond(1713000000L), 1713000001L)
        );

        List<Flight> saved = new java.util.ArrayList<>();
        FlightStore fakeStore = saved::add;

        FlightController controller = new FlightController(fakeFeeder, fakeStore);
        controller.execute();

        assert saved.size() == 2;
    }
}