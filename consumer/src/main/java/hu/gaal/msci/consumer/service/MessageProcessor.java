package hu.gaal.msci.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class MessageProcessor implements Callable<String> {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    private String message;

    public MessageProcessor(final String message) {
        this.message = message;
    }

    @Override
    public String call() {
        logger.debug("Processing: " + message);
        return message + "_processed";
    }
}
