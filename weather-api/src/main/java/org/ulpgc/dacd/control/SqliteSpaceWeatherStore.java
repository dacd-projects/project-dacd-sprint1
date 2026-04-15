package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.SpaceWeather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqliteSpaceWeatherStore implements SpaceWeatherStore {

    private static final String URL = "jdbc:sqlite:aviation.db";

    public SqliteSpaceWeatherStore() {
        initTable();
    }

    private void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS space_weather (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_type TEXT,
                    kp_index REAL,
                    start_time TEXT,
                    end_time TEXT,
                    source TEXT,
                    captured_at INTEGER
                )
                """;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(SpaceWeather spaceWeather) {
        String sql = """
                INSERT INTO space_weather
                (event_type, kp_index, start_time, end_time, source, captured_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, spaceWeather.getEventType());
            stmt.setDouble(2, spaceWeather.getKpIndex());
            stmt.setString(3, spaceWeather.getStartTime());
            stmt.setString(4, spaceWeather.getEndTime());
            stmt.setString(5, spaceWeather.getSource());
            stmt.setLong(6, spaceWeather.getCapturedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}