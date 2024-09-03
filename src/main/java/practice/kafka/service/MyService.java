package practice.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.kafka.data.MyEntity;
import practice.kafka.data.MyRepository;
import practice.kafka.model.MyModel;
import practice.kafka.model.MyModelConverter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyRepository myRepository;

    @Transactional(readOnly = true)
    public List<MyModel> findAll() {
        return myRepository.findAll().stream()
                .map(MyModelConverter::toModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public MyModel findById(Integer id) {
        return myRepository.findById(id)
                .map(MyModelConverter::toModel)
                .orElse(null);
    }

    @Transactional
    public MyModel save(MyModel myModel) {
        MyEntity myEntity = myRepository.save(MyModelConverter.toEntity(myModel));
        return MyModelConverter.toModel(myEntity);
    }

    @Transactional
    public MyModel update(Integer id, String content) {
        MyEntity myEntity = myRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
        myEntity.changeContent(content);
        return MyModelConverter.toModel(myEntity);
    }

    @Transactional
    public void delete(Integer id) {
        myRepository.deleteById(id);
    }
}
