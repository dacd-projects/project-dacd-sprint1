package org.ulpgc.dacd.model;

public record FlightRiskAlert(
        String icao,
        String callsign,
        double latitude,
        double longitude,
        double altitude,
        double kpIndex,
        String riskLevel
) {}