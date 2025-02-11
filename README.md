# 🛠️ 구현중

## 1 일차

### 초기 디자인 

|알 추가|알 깨기|알 전부 깨기|
|---|---|---|
|![image](https://github.com/user-attachments/assets/8e821856-9069-4449-9e6e-08e00fe78020)|![image](https://github.com/user-attachments/assets/6773480c-c3e4-4213-a7c4-357f2c4fe81e)|![image](https://github.com/user-attachments/assets/64556f46-ff1f-4e51-8c5b-c05f04b2aaff)|

### 시연 영상

https://github.com/user-attachments/assets/825e7783-0f58-44c9-8837-e9027a617960

알 추가, 적립 UI 구현

## 2 일차

### 구현

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

### 초기 디자인

미션 시작, 진행, 종료 UI 구현

![image](https://github.com/user-attachments/assets/9e228dfa-b045-4507-9e68-7373cb0c999e)

### 시연 영상

https://github.com/user-attachments/assets/69647709-09d9-4e71-9cd2-3b576b07f8a8

