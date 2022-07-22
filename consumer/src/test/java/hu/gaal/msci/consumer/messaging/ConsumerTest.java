package hu.gaal.msci.consumer.messaging;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Consumer.class)
public class ConsumerTest {

    @Autowired
    private Consumer consumer;

    @MockBean
    private ResultProducer resultProducer;

    @Test
    public void testEmptyMessage() throws InterruptedException {
        consumer.receive("");
        verify(resultProducer, times(0)).add(any());
    }

    @Test
    public void testNoProcessorFound() throws InterruptedException {
        consumer.receive("E1");
        verify(resultProducer, times(0)).add(any());
    }

    @Test
    public void testProcessorFound() throws InterruptedException {
        consumer.receive("A1");
        verify(resultProducer, times(1)).add(any());
    }
}
