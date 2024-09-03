package practice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyMessage;
import practice.kafka.model.MyTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyBatchConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {MyTopic.MY_JSON_TOPIC},
            groupId = "batch-test-consumer-group",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecords<String, String> messages) {
        log.info("[Batch Consumer] Message 도착! - {}", messages.count());
        messages.forEach(message -> {
            MyMessage myMessage;
            try {
                myMessage = objectMapper.readValue(message.value(), MyMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("[Batch Consumer] value = {}, offset = {}, partion = {}", myMessage, message.offset(), message.partition());
        });
    }
}
