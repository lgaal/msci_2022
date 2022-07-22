package hu.gaal.msci.producer.service;

import hu.gaal.msci.producer.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private JmsTemplate jmsTemplate;

    public MessageProducer(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessages(final List<String> messages) {
        messages.forEach(msg -> {
            LOGGER.debug("Sending message: " + msg);
            jmsTemplate.send("input", session -> session.createTextMessage(msg));
        });
        LOGGER.info(messages.size() + " messages has been sent.");
    }
}
