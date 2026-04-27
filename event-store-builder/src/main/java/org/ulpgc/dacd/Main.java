package org.ulpgc.dacd;

import org.ulpgc.dacd.control.EventStoreBuilder;

public class Main {
    public static void main(String[] args) {
        EventStoreBuilder builder = new EventStoreBuilder();
        builder.start();
    }
}
