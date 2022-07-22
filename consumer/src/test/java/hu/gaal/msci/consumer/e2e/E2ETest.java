package hu.gaal.msci.consumer.e2e;

import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class E2ETest {

    private static BrokerService brokerService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void test() {
        final List<String> inputs = Arrays.asList("A1", "B1", "B2", "C1", "A2", "C2", "D1", "C3", "A3", "D2", "D3");
        inputs.forEach(s -> toInput(s));
        final List<String> results = getResults();

        Assertions.assertEquals(inputs.stream().map(s -> s + "_processed").toList(), results);
    }

    @Test
    public void testHasInvalidValue() {
        final List<String> inputs = Arrays.asList("A1", "E1", "B1", "B2", "C1", "A2", "C2", "D1", "C3", "A3", "D2", "D3");
        inputs.forEach(s -> toInput(s));
        final List<String> results = getResults();

        Assertions.assertEquals(inputs.stream().filter(s -> !s.equals("E1")).map(s -> s + "_processed").toList(), results);
    }

    private void toInput(final String msg) {
        jmsTemplate.send("input", session -> session.createTextMessage(msg));
    }

    private List<String> getResults() {
        return jmsTemplate.execute(session -> {
            final ArrayList<String> result = new ArrayList<>();
            final MessageConsumer consumer = session.createConsumer(session.createQueue("results"));

            Message msg;
            while((msg = consumer.receive(1000)) != null)
                if (msg instanceof TextMessage) result.add(((TextMessage) msg).getText());
            return result;
        });
    }

    @BeforeAll
    public static void startBroker() throws Exception {
        brokerService = new BrokerService();
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.setPersistent(false);
        brokerService.start();
    }

    @AfterAll
    public static void stopBroker() throws Exception {
        brokerService.stop();
        brokerService = null;
    }
}
