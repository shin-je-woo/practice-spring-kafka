package practice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyMessage;
import practice.kafka.model.MyTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {MyTopic.MY_JSON_TOPIC},
            groupId = "test-consumer-group"
    )
    public void listen(ConsumerRecord<String, String> message) {
        MyMessage myMessage;
        try {
            myMessage = objectMapper.readValue(message.value(), MyMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("[Simple Consumer] Message 도착! - {}", myMessage);
    }
}
