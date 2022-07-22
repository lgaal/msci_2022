package hu.gaal.msci.producer.controller;

import hu.gaal.msci.producer.service.MessageProducer;
import hu.gaal.msci.producer.service.ResultReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private MessageProducer producer;
    private ResultReaderService resultReaderService;

    public Controller(final MessageProducer producer, final ResultReaderService resultReaderService) {
        this.producer = producer;
        this.resultReaderService = resultReaderService;
    }

    @PostMapping("/send")
    public void sendMessages(@RequestParam final List<String> messages) {
        LOGGER.info("Sending " + messages.size() + " messages...");
        producer.sendMessages(messages);
    }

    @GetMapping("/results")
    public List<String> getResults() {
        LOGGER.info("Retrieving all messages from 'results'.");
        return resultReaderService.getAllResults();
    }
}
