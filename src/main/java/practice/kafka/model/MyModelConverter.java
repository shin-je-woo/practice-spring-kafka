package practice.kafka.model;

import practice.kafka.data.MyEntity;

public class MyModelConverter {

    public static MyCdcModel toModel(MyEntity entity) {
        return new MyCdcModel(
                entity.getId(),
                entity.getUserId(),
                entity.getUserAge(),
                entity.getUserName(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static MyEntity toEntity(MyCdcModel model) {
        return new MyEntity(
                model.getId(),
                model.getUserId(),
                model.getUserAge(),
                model.getUserName(),
                model.getContent(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }

    public static MyCdcModel toModel(MyCdcMessage message) {
        return new MyCdcModel(
                message.getId(),
                message.getPayload().getUserId(),
                message.getPayload().getUserAge(),
                message.getPayload().getUserName(),
                message.getPayload().getContent(),
                message.getPayload().getCreatedAt(),
                message.getPayload().getUpdatedAt()
        );
    }

    public static MyCdcMessage toMessage(Integer id, MyCdcModel model, OperationType operationType) {
        MyCdcMessage.Payload payload = null;
        if (operationType == OperationType.CREATE || operationType == OperationType.UPDATE) { // C, U의 경우만 payload 존재
            payload = new MyCdcMessage.Payload(
                    model.getUserId(),
                    model.getUserAge(),
                    model.getUserName(),
                    model.getContent(),
                    model.getCreatedAt(),
                    model.getUpdatedAt()
            );
        }
        return new MyCdcMessage(
                id,
                payload,
                operationType
        );
    }
}
