package practice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyMessage;
import practice.kafka.model.MyTopic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyConsumer {

    private final ObjectMapper objectMapper;
    private final Map<String, Integer> idHistoryMap = new ConcurrentHashMap<>(); // Exactly Once를 보장하기 위함 (보통은 Redis를 사용하겠지만, 간단하게 함)

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
        this.printPayloadIfFirstMessage(myMessage);
    }

    private void printPayloadIfFirstMessage(MyMessage myMessage) {
        if(idHistoryMap.putIfAbsent(String.valueOf(myMessage.getId()), 1) == null) {
            log.info("[Simple Consumer] 메시지 도착! = {}", myMessage); // // Exactly Once 실행되어야 하는 로직으로 가정
        } else {
            log.info("[Simple Consumer] 메시지 중복! = {}", myMessage);
        }
    }
}
