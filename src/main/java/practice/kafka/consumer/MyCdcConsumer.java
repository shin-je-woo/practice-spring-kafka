package practice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyCdcMessage;
import practice.kafka.model.MyTopic;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyCdcConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {MyTopic.MY_CDC_TOPIC},
            groupId = "cdc-consumer-group",
            concurrency = "1"
    )
    public void listen(ConsumerRecord<String, String> message) {
        MyCdcMessage myCdcMessage;
        try {
            myCdcMessage = objectMapper.readValue(message.value(), MyCdcMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("[Cdc Consumer] {} 메시지 도착! ({}번) = {}", myCdcMessage.getOperationType(), myCdcMessage.getId(), myCdcMessage.getPayload());
    }
}
