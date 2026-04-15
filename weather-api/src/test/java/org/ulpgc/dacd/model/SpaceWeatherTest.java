package org.ulpgc.dacd.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpaceWeatherTest {

    @Test
    void spaceWeatherIsCreatedWithCorrectValues() {

        SpaceWeather event = new SpaceWeather(
                "GST-001", 6.5,
                "2024-04-01T12:00Z", "2024-04-01T18:00Z",
                "NASA", 1713000001L
        );

        assertEquals("GST-001", event.getEventType());
        assertEquals(6.5, event.getKpIndex());
        assertEquals("2024-04-01T12:00Z", event.getStartTime());
        assertEquals("2024-04-01T18:00Z", event.getEndTime());
        assertEquals("NASA", event.getSource());
        assertEquals(1713000001L, event.getCapturedAt());
    }
}