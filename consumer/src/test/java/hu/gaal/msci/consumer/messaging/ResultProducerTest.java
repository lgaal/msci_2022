package hu.gaal.msci.consumer.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ResultProducerTest {

    private JmsTemplate jmsTemplate = mock(JmsTemplate.class);

    @Test
    public void testCompletedResult() {
        final ResultProducer producer = new ResultProducer(jmsTemplate);
        producer.start();

        producer.add(CompletableFuture.completedFuture("A1_processed"));

        verify(jmsTemplate, times(1)).send(anyString(), any());
        producer.stopProducer();
    }

    @Test
    public void testNullResult() {
        final ResultProducer producer = new ResultProducer(jmsTemplate);
        producer.start();

        producer.add(CompletableFuture.failedFuture(new RuntimeException()));

        verify(jmsTemplate, times(0)).send(anyString(), any());
        producer.stopProducer();
    }
}
