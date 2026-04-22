package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Flight;

import jakarta.jms.*;

public class ActiveMQFlightStore implements FlightStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "Flight";

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

            String json = gson.toJson(flight);
            TextMessage message = session.createTextMessage(json);

            producer.send(message);

            producer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}