package practice.kafka.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import practice.kafka.model.MyModelConverter;
import practice.kafka.producer.MyCdcProducer;

@Component
@RequiredArgsConstructor
public class MyCdcApplicationListener {

    private final MyCdcProducer myCdcProducer;

    /**
     * RDB에 커밋한 이후에 Kafka에 producer하기 위해 @TransactionalEventListener 적용
     * Kafka에 produce하는 로직은 WAS 스레드와 별개로 수행하기 위해 @Async 적용
     *
     * 미사용) RDB에 커밋이 완료되었지만, Kafka produce에서 Exception 발생하면 문제 발생
     * 문제: RDB에는 데이터가 있지만, Kafka에는 데이터 없는 상황 발생
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void produceAfterCommit(MyCdcApplicationEvent event) {
        if (event.getMyCdcModel().getContent().equals("카프카 실패")) {
            throw new IllegalArgumentException("일부러 실패~");
        }
        myCdcProducer.sendMessage(
                MyModelConverter.toMessage(
                        event.getId(),
                        event.getMyCdcModel(),
                        event.getOperationType()
                )
        );
    }
}
