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
            containerFactory = "batchKafkaListenerContainerFactory",
            concurrency = "3" // concurrency가 3이므로 한번에 3개 파티션 데이터 처리 가능
    )
    public void listen(ConsumerRecords<String, String> messages) {
        log.info("[Batch Consumer({})] Message 도착! - {}", Thread.currentThread().getId(), messages.count());
        messages.forEach(message -> {
            MyMessage myMessage;
            try {
                myMessage = objectMapper.readValue(message.value(), MyMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("[Batch Consumer({})] value = {}, offset = {}, partion = {}", Thread.currentThread().getId(), myMessage, message.offset(), message.partition());
        });
    }
}
