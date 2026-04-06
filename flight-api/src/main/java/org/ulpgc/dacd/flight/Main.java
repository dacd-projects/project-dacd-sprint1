package org.ulpgc.dacd.flight;

import org.ulpgc.dacd.persistence.SchemaInitializer;

public class Main {

    public static void main(String[] args) {
        try {
            SchemaInitializer.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FlightService service = new FlightService();
        service.execute();

        System.out.println("Sistema funcionando correctamente");
    }
}

