package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Flight;

import jakarta.jms.*;

public class ActiveMQFlightStore implements FlightStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "Flight";
    private static final String SOURCE_ID = "flight-api";

    private final Gson gson = new Gson();

    @Override
    public void save(Flight flight) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);

            JsonObject event = gson.toJsonTree(flight).getAsJsonObject();
            event.addProperty("ts", flight.getCapturedAt());
            event.addProperty("ss", SOURCE_ID);

            TextMessage message = session.createTextMessage(event.toString());
            producer.send(message);

            producer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}