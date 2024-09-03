package practice.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.kafka.data.MyEntity;
import practice.kafka.data.MyRepository;
import practice.kafka.event.MyCdcApplicationEvent;
import practice.kafka.model.MyCdcModel;
import practice.kafka.model.MyModelConverter;
import practice.kafka.model.OperationType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyRepository myRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<MyCdcModel> findAll() {
        return myRepository.findAll().stream()
                .map(MyModelConverter::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public MyCdcModel findById(Integer id) {
        return myRepository.findById(id)
                .map(MyModelConverter::toModel)
                .orElse(null);
    }

    @Transactional
    public MyCdcModel save(MyCdcModel myCdcModel) {
        MyEntity myEntity = myRepository.save(MyModelConverter.toEntity(myCdcModel));
        MyCdcModel resultModel = MyModelConverter.toModel(myEntity);
        eventPublisher.publishEvent(
                MyCdcApplicationEvent.of(
                        resultModel.getId(),
                        resultModel,
                        OperationType.CREATE
                )
        );
        if (myCdcModel.getContent().equals("실패")) {
            throw new IllegalArgumentException("일부러 실패~");
        }
        return resultModel;
    }

    @Transactional
    public MyCdcModel update(Integer id, String content) {
        MyEntity myEntity = myRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        myEntity.changeContent(content);
        MyCdcModel resultModel = MyModelConverter.toModel(myEntity);
        eventPublisher.publishEvent(
                MyCdcApplicationEvent.of(
                        resultModel.getId(),
                        resultModel,
                        OperationType.UPDATE
                )
        );
        return resultModel;
    }

    @Transactional
    public void delete(Integer id) {
        myRepository.deleteById(id);
        eventPublisher.publishEvent(
                MyCdcApplicationEvent.of(
                        id,
                        null,
                        OperationType.DELETE
                )
        );
    }
}
