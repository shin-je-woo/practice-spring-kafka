package practice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyMessage;

@Slf4j
@Component
public class MyConsumer {

    @KafkaListener(
            topics = {"my-json-topic"},
            groupId = "test-consumer-group"
    )
    public void listen(ConsumerRecord<String, MyMessage> message) {
        log.info("Message 도착! - {}", message.value());
    }
}
