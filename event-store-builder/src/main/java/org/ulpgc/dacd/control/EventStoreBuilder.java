package org.ulpgc.dacd.control;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class EventStoreBuilder implements MessageListener {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID = "event-store-builder";
    private static final String[] TOPICS = {"Flight", "SpaceWeather"};

    private final FileEventStore fileEventStore = new FileEventStore();

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.setClientID(CLIENT_ID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : TOPICS) {
                Topic topic = session.createTopic(topicName);
                TopicSubscriber subscriber = session.createDurableSubscriber(topic, topicName + "-sub");
                subscriber.setMessageListener(this);
            }

            System.out.println("Event Store Builder escuchando...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                String topic = message.getJMSDestination().toString().replace("topic://", "");
                fileEventStore.save(topic, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
