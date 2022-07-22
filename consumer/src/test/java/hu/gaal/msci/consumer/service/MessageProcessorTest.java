package hu.gaal.msci.consumer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageProcessorTest {

    @Test
    public void testProcess() {
        final MessageProcessor processor = new MessageProcessor("A1");
        final String result = processor.call();

        Assertions.assertEquals("A1_processed", result);
    }
}
