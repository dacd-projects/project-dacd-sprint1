package org.ulpgc.dacd.model;

public record SpaceWeather(
        String eventType,
        double kpIndex,
        String startTime,
        String endTime,
        long ts
) {}