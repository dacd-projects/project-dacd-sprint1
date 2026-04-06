package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    public static void createTables() throws SQLException {

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flight_states (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    flight_icao TEXT,
                    airport TEXT,
                    status TEXT,
                    altitude REAL,
                    last_update TEXT,
                    captured_at TEXT
                );
            """);
        }
    }
}
