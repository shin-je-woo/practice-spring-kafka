package practice.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyCdcModel {
    private final Integer id;
    private final Integer userId;
    private final Integer userAge;
    private final String userName;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MyCdcModel of(
            Integer userId,
            Integer userAge,
            String userName,
            String content
    ) {
        return MyCdcModel.builder()
                .userId(userId)
                .userAge(userAge)
                .userName(userName)
                .content(content)
                .build();
    }
}
