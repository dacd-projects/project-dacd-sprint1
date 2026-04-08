package org.ulpgc.dacd;

import org.ulpgc.dacd.control.FlightController;
import org.ulpgc.dacd.control.FlightFeeder;
import org.ulpgc.dacd.control.FlightStore;
import org.ulpgc.dacd.control.OpenSkyFlightFeeder;
import org.ulpgc.dacd.control.SqliteFlightStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        FlightFeeder feeder = new OpenSkyFlightFeeder();
        FlightStore store = new SqliteFlightStore();
        FlightController controller = new FlightController(feeder, store);
        controller.run();

    }
}