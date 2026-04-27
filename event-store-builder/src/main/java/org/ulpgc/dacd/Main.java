package org.ulpgc.dacd;

import org.ulpgc.dacd.control.EventStoreBuilder;

public class Main {
    public static void main(String[] args) {
        String brokerUrl = args[0];
        String clientId = args[1];
        String[] topics = args[2].split(",");

        EventStoreBuilder builder = new EventStoreBuilder(brokerUrl, clientId, topics);
        builder.start();
    }
}