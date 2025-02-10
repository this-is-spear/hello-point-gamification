# 🛠️ 구현중

## 1 일차

https://github.com/user-attachments/assets/825e7783-0f58-44c9-8837-e9027a617960

알 추가, 적립 UI 구현

## 2 일차

알 추가, 적립 데이터 저장 구현

- GameSessionStarted : 게임 시작 이벤트
- EggsAcquired : 알 획득 이벤트
- EggBroken : 알 꺠기 이벤트 -> 포인트 적립으로 이어짐.

<img width="1578" alt="스크린샷 2025-02-11 오전 12 20 27" src="https://github.com/user-attachments/assets/061d0ee7-58b6-4267-870e-467ce152dae9" />
<img width="1568" alt="스크린샷 2025-02-11 오전 12 20 41" src="https://github.com/user-attachments/assets/fa39848f-f741-4d36-b7a7-36a258cf3075" />

세션으로 유지되는 게임이 Aggregate로 선정했다.
Aggregate가 유지되는 동안 사용 가능한 알 개수와 포인트가 기록된다.

```java
public class MyGameSession {
    private String sessionId;
    private int availableEggs;
    private int availablePoints;
}
```

> Aggregate를 거쳐 알과 포인트 상태가 변경되도록 구현했다.

## 3 일차

미션 시작, 진행, 종료 UI 구현

https://github.com/user-attachments/assets/69647709-09d9-4e71-9cd2-3b576b07f8a8

