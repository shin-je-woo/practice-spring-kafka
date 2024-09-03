package practice.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyModel {
    private final Integer id;
    private final Integer userId;
    private final Integer userAge;
    private final String userName;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MyModel of(
            Integer userId,
            Integer userAge,
            String userName,
            String content
    ) {
        return MyModel.builder()
                .userId(userId)
                .userAge(userAge)
                .userName(userName)
                .content(content)
                .build();
    }
}
