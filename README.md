# practice-spring-kafka
🧨 학습목적 - 스프링 카프카

Spring for Apache Kafka를 사용해서 스프링 환경에서 카프카 사용해보기

## 학습 내용

- Spring-Kafka 환경 구성
- 기본적인 Producer, Consumer
- 배치리스너
- 자동커밋, 수동커밋
- 멱등성 보장
- 애플리케이션 level의 CDC(Change Data Capture)

### CDC 문제상황

- { RDB 데이터 저장 -> Kafka 이벤트 발행 } 과 같은 하나의 flow가 있을 때, 단순하게 @Transactional 안에서 처리하면 문제가 발생할 수 있다.
- 해당 flow에서 Kafka 이벤트가 발행되고 나서 Exception이 발생하여 RDB 데이터는 rollback되어도 Kafka에서 발행한 이벤트는 rollback되지 않는다.
- 즉, RDB 데이터 저장과 Kafka 이벤트 발행 연산의 원자성이 보장되지 않는다.

#### 생각해보기

- 스프링 이벤트 발행(@TransactionalEventListener)을 사용해 RDB에 커밋된 이후에 Kafka에 produce 하면?
  - 이 방법 또한 Kafka에 produce 할 때 Exception이 발생하면 RDB에 커밋되고, Kafka에는 produce가 안되는 문제가 있다.
  - 즉, 이 방법 또한 원자성이 보장되지 않는다.