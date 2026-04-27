package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.SpaceWeather;

import jakarta.jms.*;

public class ActiveMQSpaceWeatherStore implements SpaceWeatherStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "SpaceWeather";
    private static final String SOURCE_ID = "weather-api";

    private final Gson gson = new Gson();

    @Override
    public void save(SpaceWeather event) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);

            JsonObject json = gson.toJsonTree(event).getAsJsonObject();
            json.addProperty("ts", event.getCapturedAt());
            json.addProperty("ss", SOURCE_ID);

            TextMessage message = session.createTextMessage(json.toString());
            producer.send(message);

            producer.close();
            session.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}