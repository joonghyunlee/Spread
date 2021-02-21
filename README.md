# Spread

뿌리기 API



## 개요

* Division: 뿌리기 요청
  * 임의의 사용자에 의해 생성
* Dividend: 가져가기 요청



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