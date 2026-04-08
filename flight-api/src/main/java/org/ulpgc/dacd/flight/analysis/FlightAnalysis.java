package org.ulpgc.dacd.flight.analysis;

import org.ulpgc.dacd.persistence.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FlightAnalysis {

    public void runAnalysis() {

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Total vuelos
            ResultSet total = stmt.executeQuery("SELECT COUNT(*) FROM flights");
            System.out.println("Total vuelos: " + total.getInt(1));

            // Altitud media
            ResultSet avgAlt = stmt.executeQuery("SELECT AVG(altitude) FROM flights");
            System.out.println("Altitud media: " + avgAlt.getDouble(1));

            // Vuelos con baja altitud
            ResultSet lowAlt = stmt.executeQuery("SELECT COUNT(*) FROM flights WHERE altitude < 8000");
            System.out.println("Vuelos con posible desviación: " + lowAlt.getInt(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
