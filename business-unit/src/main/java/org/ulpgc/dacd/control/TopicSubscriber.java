package org.ulpgc.dacd.control;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubscriber {

    private static final String CLIENT_ID = "business-unit";
    private final String brokerUrl;
    private final String[] topics;
    private final DatamartUpdater datamartUpdater;

    public TopicSubscriber(String brokerUrl, String[] topics, DatamartUpdater datamartUpdater) {
        this.brokerUrl = brokerUrl;
        this.topics = topics;
        this.datamartUpdater = datamartUpdater;
    }

    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = factory.createConnection();
            connection.setClientID(CLIENT_ID);
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : topics) {
                Topic topic = session.createTopic(topicName);
                jakarta.jms.TopicSubscriber subscriber = session.createDurableSubscriber(topic, topicName + "-business-unit");
                subscriber.setMessageListener(message -> {
                    try {
                        if (message instanceof TextMessage textMessage) {
                            datamartUpdater.process(textMessage.getText());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            System.out.println("Business Unit suscrito a los topics: " + String.join(", ", topics));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}