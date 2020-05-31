# 의존성 관리 응용

## 스프링 부트가 지원하는 의존성 버전 바꾸기

version을 일괄적으로 바꾸고 싶으면 `pom.xml`에 `properties`를 추가해 버전을 명시해 준다.

```xml
<properties>
    <spring-batch.version>4.2.1.RELEASE</spring-batch.version>
</properties>
```