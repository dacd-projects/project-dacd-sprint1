package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FlightRepository {

    public void saveFlight(String icao,
                           String callsign,
                           String country,
                           double altitude,
                           long lastUpdate) throws Exception {

        String sql = """
        INSERT INTO flights
        (icao, callsign, country, altitude, last_update)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, icao);
            stmt.setString(2, callsign);
            stmt.setString(3, country);
            stmt.setDouble(4, altitude);
            stmt.setLong(5, lastUpdate);

            stmt.executeUpdate();
        }
    }
}