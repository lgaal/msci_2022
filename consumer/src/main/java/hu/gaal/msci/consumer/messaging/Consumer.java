package hu.gaal.msci.consumer.messaging;

import hu.gaal.msci.consumer.service.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

@Component
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private final ResultProducer producer;
    private final Map<Character, ExecutorCompletionService> processors;

    public Consumer(final ResultProducer producer) {
        this.producer = producer;

        final ExecutorCompletionService<String> processorAB =
                new ExecutorCompletionService<>(Executors.newFixedThreadPool(3));
        final ExecutorCompletionService<String> processorCD =
                new ExecutorCompletionService<>(Executors.newFixedThreadPool(3));

        processors = new HashMap<>();
        processors.put('A', processorAB);
        processors.put('B', processorAB);
        processors.put('C', processorCD);
        processors.put('D', processorCD);

        producer.start();
    }

    @JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "input")
    public void receive(final String message) throws InterruptedException {
        LOGGER.debug("Message received: " + message);
        if (message.length() < 1) {
            LOGGER.warn("Unprocessable message: " + message);
            return;
        }
        final ExecutorCompletionService processor = processors.get(message.charAt(0));
        if (processor == null) {
            LOGGER.warn("Unprocessable message: " + message);
            return;
        }

        producer.add(processor.submit(new MessageProcessor(message)));
    }
}

