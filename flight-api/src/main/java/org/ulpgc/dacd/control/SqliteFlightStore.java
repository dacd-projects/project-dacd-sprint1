package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Flight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqliteFlightStore implements FlightStore {

    private static final String URL = "jdbc:sqlite:aviation.db";

    public SqliteFlightStore() {
        initTable();
    }

    private void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS flights (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    icao TEXT,
                    callsign TEXT,
                    country TEXT,
                    latitude REAL,
                    longitude REAL,
                    altitude REAL,
                    velocity REAL,
                    last_update TEXT,
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
    public void save(Flight flight) {
        String sql = """
                INSERT INTO flights
                (icao, callsign, country, latitude, longitude, altitude, velocity, last_update, captured_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getIcao());
            stmt.setString(2, flight.getCallsign());
            stmt.setString(3, flight.getCountry());
            stmt.setDouble(4, flight.getLatitude());
            stmt.setDouble(5, flight.getLongitude());
            stmt.setDouble(6, flight.getAltitude());
            stmt.setDouble(7, flight.getVelocity());
            stmt.setString(8, flight.getLastUpdate().toString());
            stmt.setLong(9, flight.getCapturedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}