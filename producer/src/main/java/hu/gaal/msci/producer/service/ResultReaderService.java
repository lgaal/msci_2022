package hu.gaal.msci.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service
public class ResultReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultReaderService.class);

    private JmsTemplate jmsTemplate;

    public ResultReaderService(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public List<String> getAllResults() {
        return jmsTemplate.browse("results", (session, browser) -> {
            final ArrayList<String> results = new ArrayList<>();

            final Enumeration enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                final Object element = enumeration.nextElement();
                if (element instanceof TextMessage)
                    results.add(((TextMessage) element).getText());
            }

            return results;
        });
    }
}
