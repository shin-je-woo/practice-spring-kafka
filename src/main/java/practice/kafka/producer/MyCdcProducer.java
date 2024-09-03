package practice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyCdcMessage;
import practice.kafka.model.MyTopic;

@Component
@RequiredArgsConstructor
public class MyCdcProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(MyCdcMessage message) {
        String myMessage;
        try {
            myMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(
                MyTopic.MY_CDC_TOPIC,
                String.valueOf(message.getId()),
                myMessage);
    }
}
