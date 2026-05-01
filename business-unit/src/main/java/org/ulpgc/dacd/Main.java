package org.ulpgc.dacd;

import org.ulpgc.dacd.control.AlertService;
import org.ulpgc.dacd.control.DatamartUpdater;
import org.ulpgc.dacd.control.EventStoreReader;
import org.ulpgc.dacd.control.TopicSubscriber;
import org.ulpgc.dacd.rest.RestApi;
import org.ulpgc.dacd.store.DatamartStore;

public class Main {
    public static void main(String[] args) {
        String brokerUrl = args[0];
        String eventStorePath = args[1];
        String[] topics = {"Flight", "SpaceWeather"};

        DatamartStore datamartStore = new DatamartStore();
        DatamartUpdater datamartUpdater = new DatamartUpdater(datamartStore);

        EventStoreReader eventStoreReader = new EventStoreReader(eventStorePath, datamartUpdater);
        eventStoreReader.loadHistory();

        TopicSubscriber subscriber = new TopicSubscriber(brokerUrl, topics, datamartUpdater);
        subscriber.start();

        AlertService alertService = new AlertService(datamartStore);
        RestApi restApi = new RestApi(datamartStore, alertService);
        restApi.start(8080);
    }
}