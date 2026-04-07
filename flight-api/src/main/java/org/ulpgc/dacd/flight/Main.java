package org.ulpgc.dacd.flight;

import org.ulpgc.dacd.persistence.SchemaInitializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        try {
            SchemaInitializer.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FlightService service = new FlightService();

        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        System.out.println("Sistema iniciado correctamente");

        scheduler.scheduleAtFixedRate(
                service::execute,
                0,
                1,
                TimeUnit.HOURS
        );
    }
}

