package practice.kafka.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.kafka.model.MyMessage;
import practice.kafka.producer.MyProducer;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyProducer myProducer;

    @PostMapping("/message")
    void message(
            @RequestBody MyMessage message
    ) {
        myProducer.sendMessage(message);
    }
}
