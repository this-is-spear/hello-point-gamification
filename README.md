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


## 4 일차

미션 관련 API 연결 동작을 구현했다.

### 구현
추가된 API는 다음과 같다.

- 미션 리스트 조회
- 미션 세부 내용 조회
- 미션 시작
- 미션 종료

미션이 종료되면 보상을 제공하도록 하드 코딩했다.

### 시연 영상

https://github.com/user-attachments/assets/472fa58c-2c77-4c63-8761-f5dca10f326f

### 여기에서 든 궁금증.

- 완료되면서 보상으로 알 적립을 JS로 구현했다. 만약 포인트로 보상을 제공하고 싶다면 JS에서 변경해야 한다. 맞는 행동처럼 보이지 않는다. 미션이 완료되면 이벤트로 보상하고 싶은 모듈에 전달해야 하는게 아닐까 생각든다.
- 미션이라는 도메인이 있어. 관리자가 미션을 등록하는 것과 사용자가 시작하는 미션은 엄연히 다르다. 등록한 미션을 가져와 개개인이 사용하는 문제가 있기 때문이다.

둘 사이에 차이점을 만들기 위한 단어가 있을까?
관리자 입장에서 미션을 어떤 단어로 표현할 수 있을까?
사용자 입장에서 미션을 어떤 단어로 표현할 수 있을까?

![image](https://github.com/user-attachments/assets/564d8243-5054-43b6-8d71-2b9efc7e6fd9)

## 5 일차

상태 변경마다 게임 정보 API를 호출하는 로직을 SSE 방식으로 전환했다.

