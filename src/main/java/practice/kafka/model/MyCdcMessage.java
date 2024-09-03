package practice.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyCdcMessage {
    private int id;
    private Payload payload;
    private OperationType operationType;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Payload {
        private int userId;
        private int userAge;
        private String userName;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
