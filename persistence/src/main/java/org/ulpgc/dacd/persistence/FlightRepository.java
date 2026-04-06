package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;

public class FlightRepository {

    public void saveFlight() throws Exception {

        String sql = """
            INSERT INTO flight_states
            (flight_icao, airport, status, altitude, last_update, captured_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "UNKNOWN");
            stmt.setString(2, "GLOBAL");
            stmt.setString(3, "ACTIVE");
            stmt.setDouble(4, 0.0);
            stmt.setString(5, Instant.now().toString());
            stmt.setString(6, Instant.now().toString());

            stmt.executeUpdate();
        }
    }
}
