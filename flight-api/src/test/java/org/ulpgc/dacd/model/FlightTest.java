package org.ulpgc.dacd.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {

    @Test
    void flightIsCreatedWithCorrectValues() {

        Flight flight = new Flight(
                "abc123", "ANC001", "Norway",
                70.5, 25.3, 10000.0,
                250.0, Instant.ofEpochSecond(1713000000L), 1713000001L
        );

        assertEquals("abc123", flight.getIcao());
        assertEquals("ANC001", flight.getCallsign());
        assertEquals("Norway", flight.getCountry());
        assertEquals(70.5, flight.getLatitude());
        assertEquals(25.3, flight.getLongitude());
        assertEquals(10000.0, flight.getAltitude());
        assertEquals(250.0, flight.getVelocity());
        assertEquals(Instant.ofEpochSecond(1713000000L), flight.getLastUpdate());
        assertEquals(1713000001L, flight.getCapturedAt());
    }
}
