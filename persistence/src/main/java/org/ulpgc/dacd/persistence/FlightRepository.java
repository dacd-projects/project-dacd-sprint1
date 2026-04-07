package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FlightRepository {

    public void saveFlight(String icao,
                           String callsign,
                           String country,
                           double latitude,
                           double longitude,
                           double altitude,
                           double velocity,
                           long lastUpdate,
                           long capturedAt) throws Exception {

        String sql = """
            INSERT INTO flights
            (icao, callsign, country, latitude, longitude, altitude, velocity, last_update, captured_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, icao);
            stmt.setString(2, callsign);
            stmt.setString(3, country);
            stmt.setDouble(4, latitude);
            stmt.setDouble(5, longitude);
            stmt.setDouble(6, altitude);
            stmt.setDouble(7, velocity);
            stmt.setLong(8, lastUpdate);
            stmt.setLong(9, capturedAt);

            stmt.executeUpdate();
        }
    }
}