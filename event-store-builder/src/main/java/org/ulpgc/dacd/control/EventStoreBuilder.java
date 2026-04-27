package org.ulpgc.dacd.control;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class EventStoreBuilder implements MessageListener {

    private final String brokerUrl;
    private final String clientId;
    private final String[] topics;

    private final FileEventStore fileEventStore = new FileEventStore();

    public EventStoreBuilder(String brokerUrl, String clientId, String[] topics) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
        this.topics = topics;
    }

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : topics) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer consumer = session.createConsumer(topic);
                consumer.setMessageListener(this);
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