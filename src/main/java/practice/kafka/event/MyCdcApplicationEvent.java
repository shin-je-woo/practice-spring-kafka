package practice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import practice.kafka.model.MyCdcModel;
import practice.kafka.model.OperationType;

@Getter
@AllArgsConstructor
public class MyCdcApplicationEvent {
    private final Integer id;
    private final MyCdcModel myCdcModel;
    private final OperationType operationType;

    public static MyCdcApplicationEvent of(Integer id, MyCdcModel myCdcModel, OperationType operationType) {
        return new MyCdcApplicationEvent(id, myCdcModel, operationType);
    }
}
