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

다른 모듈 간 상태를 호출해야 하는 문제가 있다. `미션 보상 후 알 적립`, `알 깨기 후 포인트 적립` 기능은 다른 모듈 정보에 접근해서 상태 변경 코드를 호출해야 했다.

- Mission : 미션 정보가 포함된다.
- Egg : 알 정보가 포함된다.
- Point : 포인트 정보가 포함된다.

두 클래스 간 결합력이 생기게 된다.

|알 깨기 후 포인트 적립 상황|미션 완료 후 알 적립 상황|
|---|---|
|![image](https://github.com/user-attachments/assets/32488ebb-ecf8-45c6-b107-3ca8d5d9f99c)|![image](https://github.com/user-attachments/assets/bb53d9f5-f5c8-4a9d-9a9d-e63905b26cc5)|

`미션 클래스`와 `알 클래스`가 연결되면서 자바스크립트가 실행 흐름을 가지게 됐다고 판단했다.
실행 흐름은 클래스간 결합력을 만들어낸다. 결합력이 많아지면 코드 관리가 어려워진다. 그래서 SSE를 이용해 상태 변경을 외부로 추출했다.

사례로 `미션 보상 후 알 적립` 과정을 가져왔다. 이전에는 미션에서 알 상태 변화를 알려줘야 했고 그로 인해 결합력이 생겼던 문제를 해결했다.

|AS-IS|TO-BE|
|---|---|
|![image](https://github.com/user-attachments/assets/881375ef-6ef0-4b9d-ba68-01b6ff2e1785)|![image](https://github.com/user-attachments/assets/9b6996a5-5037-437d-b7fa-78f940169b0f)|

전체 결합도를 해소한 과정이다.

|AS-IS|TO-BE|
|---|---|
|![image](https://github.com/user-attachments/assets/1e03fae3-8a29-4a2b-bb6d-afaf5e19b2e5)|![image](https://github.com/user-attachments/assets/d7306cd4-07d0-44e4-bcc6-11b7c990f9f9)|

앞으로 `포인트 사용 기능`, `미션에서 알 적립이 아닌 포인트 적립이 가능하도록 변경`하는 등의 동작에 쉽게 대응 가능해 보인다.

`미션 후 알 적립` 기능을 구현했다면 이후에 `미션 후 포인트 적립`은 어떻게 대응하지? 고민을 했다.
결합력으로 인해 실행 흐름이 하드 코딩 돼 있다면 시간 흐름에 따라 변경이 어려워질 수 있음을 경험했다.

결합력 제거 또는 책임 분리는 `연관 없는 기능 연결로 인해 코드가 복잡해지는 상황을 예방`하기 위함임을 경험한 하루다.

## 6 일차

미션 진행 과정을 구현했다.

1. 사용자는 등록된 미션을 시작한다.
2. 사용자는 등록된 미션을 완료한다.
3. 사용자는 미션 보상을 받는다.

- 첫 번째 고민 : 어디까지 이벤트로 기록해야 할까?
  - 미션 등록하는 과정도 이벤트가 필요할까?
- 두 번째 고민 : 이벤트 주체자는 누구여야할까?
  - 사용자가 미션을 진행하고 종료하는 과정을 하나의 이벤트에 기록해야할까? -> 애그리게이트 기준으로 준비해야 한다고 생각한다. multi entity 를 가지고 있다면 해당 방법이 옳다.
  - 책에서는 이벤트 당 100개 정도 이루어지니 클러스터링이 쉽게 된다고 했다. 만약 사용자의 전체 행동을 이벤트로 묶는다면 클러스터링이 어려워진다.
