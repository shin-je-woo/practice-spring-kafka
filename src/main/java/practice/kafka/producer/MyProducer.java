package practice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyMessage;

@Component
@RequiredArgsConstructor
public class MyProducer {

    private final KafkaTemplate<String, MyMessage> kafkaTemplate;

    public void sendMessage(MyMessage message) {
        kafkaTemplate.send("my-json-topic", String.valueOf(message.getAge()), message);
    }
}
