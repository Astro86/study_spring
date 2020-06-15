# Spring Security


## 스프링 폼 인증
- 스프링 시큐리티 연동
- [스프링 시큐리티 설정하기](start_security/README.md)
- [스프링 시큐리티 커스터마이징: 인메모리 유저 추가](add_in_memory_user/README.md)
- [스프링 시큐리티 커스터마이징: JPA 연동](connect_jpa/README.md)
- [스프링 시큐리티 커스터마이징: PasswordEncoder](passwordEncoder/README.md)
- [스프링 시큐리티 테스트](security_test/README.md)

## 스프링 시큐리티 : 아키텍처

- SecurityContextHolder와 Authentication
- AuthenticationManager와 Authentication
- ThreadLocal
- Authentication과 SecurityContextHodler
- 스프링 시큐리티 필터와 FilterChainProxy
- DelegatingFilterProxy와 FilterChainProxy
- AccessDecisionManager 1부
- AccessDecisionManager 2부
- FilterSecurityInterceptor
- ExceptionTranslationFilter
- 스프링 시큐리티 아키텍처 정리


## 웹 애플리케이션 세큐리티

- 스프링 시큐리티 ignoring() 1부
- 스프링 시큐리티 ignoring() 2부
- Async 웹 MVC를 지원하는 필터: WebAsyncManagerIntegrationFilter
- 스프링 시큐리티와 @Async
- SecurityContext 영속화 필터: SecurityContextPersistenceFilter
- 시큐리티 관련 헤더 추가하는 필터: HeaderWriterFilter
- CSRF 어택 방지 필터: CsrfFilter
- CSRF 토큰 사용 예제
- 로그아웃 처리 필터: LogoutFilter
- 폼 인증 처리 필터: UsernamePasswordAuthenticationFilter
- 로그인/로그아웃 폼 페이지 생성해주는 필터: DefaultLogin/LogoutPageGeneratingFilter
- 로그인/로그아웃 폼 커스터마이징
- Basic 인증 처리 필터: BasicAuthenticationFilter
- 요청 캐시 필터: RequestCacheAwareFilter
- 시큐리티 관련 서블릿 스팩 구현 필터: SecurityContextHolderAwareRequestFilter
- 익명 인증 필터: AnonymousAuthenticationFilter
- 세션 관리 필터: SessionManagementFilter
- 인증/인가 예외 처리 필터: ExceptionTranslationFilter
- 인가 처리 필터: FilterSecurityInterceptor
- 토큰 기반 인증 필터 : RememberMeAuthenticationFilter
- 커스텀 필터 추가하기
