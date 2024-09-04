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
- 컨슈머 에러 핸들링

## CDC 문제상황

- { RDB 데이터 저장 -> Kafka 이벤트 발행 } 과 같은 하나의 flow가 있을 때, 단순하게 @Transactional 안에서 처리하면 문제가 발생할 수 있다.
- 해당 flow에서 Kafka 이벤트가 발행되고 나서 Exception이 발생하여 RDB 데이터는 rollback되어도 Kafka에서 발행한 이벤트는 rollback되지 않는다.
- 즉, RDB 데이터 저장과 Kafka 이벤트 발행 연산의 원자성이 보장되지 않는다.

### 생각해보기

- 스프링 이벤트 발행(`@TransactionalEventListener`)을 사용해 RDB에 커밋된 이후에 Kafka에 produce 하면?
  - 이 방법은 Kafka에 produce 할 때 Exception이 발생하면 RDB에 커밋되고, Kafka에는 produce가 안되는 문제가 있다.
  - 즉, 이 방법은 케이스에 따라 원자성이 보장되지 않는다.
- JPA `EntityListener`를 사용해 엔티티의 변경사항에 따라 Kafka에 produce하면?
  - 엔티티가 변경될 때 Kafka에 produce하면 영속성 컨텍스트의 쓰기지연 특성에 따라 Kafka에 이벤트 발행이 성공하면 영속성 컨텍스트 flush가 발생하면서 RDB에도 저장되고, Kafka에도 정상적으로 이벤트가 발행된다.
  - 만약, Kafka 이벤트 발행에 실패한다면 Transaction 에서 rollback될 것이기 때문에 원자성이 어느정도 보장된다.
  - 그러나, 이 방법은 JPA라는 특정 기술에 의존적이게 된다.

### 결론

- CDC는 카프카 커넥트를 이용하는게 가장 좋다고 생각된다.
- 만약, 어떤 이유로 커넥트를 운영할 수 없는 상황에서는 선택을 해야 하는데, 위에 두 방법 중에서는 JPA의 EntityListner가 조금 더 좋아 보인다.
- 스프링의 `@TransactionalEventListener`도 좋은 방법이지만, Kafka 이벤트 발행에 오류가 발생하면 데이터가 RDB에 데이터가 이미 commit된 상황이 되어버리기 때문에 해결이 어렵다.
- `EntityListener`는 JPA라는 기술에 의존적이게 되지만, 애플리케이션 level에서 CDC를 구현할 때 좋은 선택이 될 수 있다.

## 컨슈머 에러 핸들링

### 생각해보기

- 컨슈머에서 에러가 발생하면 어떻게 할까?
- 스프링 카프카의 기본 정책은 1초마다 10번 재시도 이후 해결되지 않으면 해당 레코드를 skip한다.
- 해결 방법은 2가지가 있을 것 같다.
  - 특정 횟수만큼 재시도 하고, 해결되지 않으면 skip하는 것이 아니라 컨슈머를 중지한다.
  - 특정 횟수만큼 재시도 하고, 해결되지 않으면 DLT(Dead Letter Topic)에 이동시키고 skip한다.
- 첫 번째 방법은 컨슘하지 못한 레코드를 개발자가 빠르게 인지할 수 있다는 장점이 있다. 다만, 빨리 해결하지 않으면 컨슈머 랙이 늘어날 수 있다는 단점이 있다.
- 두 번째 방법은 관리할 Topic이 하나 더 늘어나지만, 레코드를 유실시키지 않고 처리량을 유지할 수 있다는 장점이 있다.