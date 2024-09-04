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

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyCdcConsumer {

    private final ObjectMapper objectMapper;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    @KafkaListener(
            topics = {MyTopic.MY_CDC_TOPIC},
            groupId = "cdc-consumer-group",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message) {
        MyCdcMessage myCdcMessage;
        try {
            myCdcMessage = objectMapper.readValue(message.value(), MyCdcMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("재시도 횟수 = {}", retryCount.getAndIncrement());
        if (myCdcMessage.getPayload().getContent().equals("컨슈머 실패")) {
            throw new RuntimeException("일부러 실패~");
        }
        log.info("[Cdc Consumer] {} 메시지 도착! ({}번) = {}", myCdcMessage.getOperationType(), myCdcMessage.getId(), myCdcMessage.getPayload());
    }
}
