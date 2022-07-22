package hu.gaal.msci.consumer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ResultProducer extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultProducer.class);

    private final ArrayBlockingQueue<Future<String>> results = new ArrayBlockingQueue<>(100);
    private JmsTemplate jmsTemplate;
    private boolean run = true;

    public ResultProducer(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void add(final Future<String> result) {
        results.add(result);
    }

    @Override
    public void run() {
        while(run) {
            try {
                final Future<String> result = results.take();
                final String processedMessage = result.get();

                LOGGER.debug("Producing result: " + processedMessage);
                jmsTemplate.send("results", s -> s.createTextMessage(processedMessage));
            } catch (final InterruptedException | ExecutionException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void stopProducer() {
        this.run = false;
    }
}
