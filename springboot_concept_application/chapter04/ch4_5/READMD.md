# 로깅 1부 : 스프링 부트 기본 로거 설정

로깅 퍼사드 VS 로거
Commons Logging, SLF4j
JUL, Log4J2, Logback
스프링 5에 로거 관련 변경 사항
https://docs.spring.io/spring/docs/5.0.0.RC3/spring-framework-reference/overview.html#overview-logging
Spring-JCL
Commons Logging -> SLF4j or Log4j2
pom.xml에 exclusion 안해도 됨.
스프링 부트 로깅
기본 포맷
--debug (일부 핵심 라이브러리만 디버깅 모드로)
--trace (전부 다 디버깅 모드로)
컬러 출력: spring.output.ansi.enabled
파일 출력: logging.file 또는 logging.path
로그 레벨 조정: logging.level.패지키 = 로그 레벨

스프링 부트는 기본적으로 Commons Logging을 사용한다.
결국에는 SLF4j를 사용한다.(이 프로젝트에서도 SLF4j를 사용)
Commons Logging과 SLF4j는 실제 로깅을 하는 것이 아닌 로거 API를 추상해 해 놓은 인터페이스 들이다.  
주로 프레임 워크들은 로깅 퍼사드를 이용하여 코딩한다.
로깅 퍼사드의 역할은 로거를 바꿔낄 수 있게 해준다.
프레임워크를 사용하는 애플리케이션들이 로거를 자기가 원하는 것을 사용할 수 있도록 해준다.
스프링코어가 만들어질때 쯤 Commons Logging을 사용했어서 스프링 프레임워크는 Commons Logging을 사용하고 있다.
