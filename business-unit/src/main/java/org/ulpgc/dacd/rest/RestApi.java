package org.ulpgc.dacd.rest;

import io.javalin.Javalin;
import org.ulpgc.dacd.control.AlertService;
import org.ulpgc.dacd.store.DatamartStore;

public class RestApi {

    private final DatamartStore datamartStore;
    private final AlertService alertService;

    public RestApi(DatamartStore datamartStore, AlertService alertService) {
        this.datamartStore = datamartStore;
        this.alertService = alertService;
    }

    public void start(int port) {
        Javalin app = Javalin.create().start(port);

        app.get("/flights", ctx -> ctx.json(datamartStore.getActiveFlights()));

        app.get("/weather", ctx -> ctx.json(datamartStore.getLatestSpaceWeather()));

        app.get("/alerts", ctx -> ctx.json(alertService.getFlightsAtRisk()));

        System.out.println("REST API disponible en http://localhost:" + port);
    }
}
