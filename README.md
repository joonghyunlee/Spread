# Spread

뿌리기 API



## 개요

### 용어

* Division: 뿌리기 요청
  * 임의의 사용자에 의해 생성
  * 생성시간, 대화방/소유주 ID, 뿌릴 금액, 뿌릴 대상 인원 수
* Dividend: 가져가기 요청
  * Division 생성자와 같은 대화방에 속한 사용자
  * 요청자, 요청 금액

### 동작 방식

* Division 생성 요청 후 임의의 Token ID 발급

  * Token ID가 Division 객체의 지시자가 됨

* Division 가져가기 요청이 들어오면 매개변수 검증

* 이후 해당 Division에 속한 Dividend 목록을 모두 조회하여 현재까지 수령한 금액 및 수령인원을 산출

* 최초 뿌릴 금액에서 수령한 금액을 제한 후 남은 인원 만큼 나누어 가져가도록 함

  ```
  가지겨가 요청자가 획득하는 금액: (최초 뿌릴 금액 - 현재까지 수령한 금액) / (최초 뿌릴 대상 인원 수 - 현재까지 수령한 인원)
  ```



## 구동 방법

```bash
mvn spring-boot:run
```



> 기본적으로 "local", "service" 프로파일로 구성됨
>
> "service" 프로파일로 구동할 경우 MySQL DB가 같이 구동되어야 함



## API 명세

* Swagger UI

* 로컬 환경에서 구동 후 다음 경로의 페이지로 접근 후 확인 가능

  ```
  http://localhost:8080/swagger-ui.html
  ```

  

## 패키지 구조

> {project}/src/main/java/org/joonghyunlee/spread/API
>
> |---- /common: 프로젝트 공통 패키지
>
> |       |---- config: 설정 파일
>
> |       |---- exception: 서비스 예외 처리에 관련된 코드
>
> |---- /domain
>
> |        |---- division: Division 생성/조회/인출 관련 Service 계층 로직
>
> |---- /support
>
> |        |---- division: Division/Dividend 관련 Persistence 계층 로직
>
> |---- /web: Divsion API 핸들러