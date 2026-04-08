package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SpaceWeatherRepository {

    public void saveEvent(String eventType,
                          double kpIndex,
                          String startTime,
                          String endTime,
                          String source,
                          long capturedAt) throws Exception {
        String sql = """
            INSERT INTO space_weather
            (event_type, kp_index, start_time, end_time, source, captured_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventType);
            stmt.setDouble(2, kpIndex);
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            stmt.setString(5, source);
            stmt.setLong(6, capturedAt);

            stmt.executeUpdate();
        }
    }
}