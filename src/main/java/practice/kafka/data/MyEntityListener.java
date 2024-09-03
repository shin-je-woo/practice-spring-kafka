package practice.kafka.data;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import practice.kafka.model.MyCdcModel;
import practice.kafka.model.MyModelConverter;
import practice.kafka.model.OperationType;
import practice.kafka.producer.MyCdcProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyEntityListener {

    private final MyCdcProducer myCdcProducer;

    @PostPersist
    public void handleCreate(MyEntity myEntity) {
        log.info("handleCreate entered.");
        if (myEntity.getContent().equals("카프카 실패")) {
            throw new IllegalArgumentException("일부러 실패~");
        }
        MyCdcModel myCdcModel = MyModelConverter.toModel(myEntity);
        myCdcProducer.sendMessage(
                MyModelConverter.toMessage(myEntity.getId(), myCdcModel, OperationType.CREATE)
        );
    }

    @PostUpdate
    public void handleUpdate(MyEntity myEntity) {
        log.info("handleUpdate entered.");
        if (myEntity.getContent().equals("카프카 실패")) {
            throw new IllegalArgumentException("일부러 실패~");
        }
        MyCdcModel myCdcModel = MyModelConverter.toModel(myEntity);
        myCdcProducer.sendMessage(
                MyModelConverter.toMessage(myEntity.getId(), myCdcModel, OperationType.UPDATE)
        );
    }

    @PostRemove
    public void handleDelete(MyEntity myEntity) {
        log.info("handleDelete entered.");
        if (myEntity.getContent().equals("카프카 실패")) {
            throw new IllegalArgumentException("일부러 실패~");
        }
        myCdcProducer.sendMessage(
                MyModelConverter.toMessage(myEntity.getId(), null, OperationType.DELETE)
        );
    }
}
