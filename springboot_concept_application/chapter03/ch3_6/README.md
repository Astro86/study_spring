# 내장 웹 서버 이해

## 내장 서버를 스프링 부트 없이 만들어 보기

> springbootgetttingstarted

```java
public class Application {
    public static void main(String[] args) throws LifecycleException {
        // 톰캣 만들기
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8888);

        Context context = tomcat.addContext("/", "/");

        // servlet 만들기
        HttpServlet servlet = new HttpServlet(){
            @Override
            // get요청에 관한 메소드
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                // response 객체의 writer객체를 반환받아
                // 응답으로 전해줄 servlet을 작성해준다.
                PrintWriter writer = resp.getWriter();
                writer.println("<html><head><title>");
                writer.println("Hey, Tomcat");
                writer.println("</title></head>");
                writer.println("<body><h1>Hello Tomcat</h1></body>");
                writer.println("</html>");
            }
        };

        String servletName = "helloServlet";
        // 톰켓에 서블릿을 추가해준 후
        tomcat.addServlet("/", servletName, servlet);
        // /hello url과 해당 서블릿을 매핑 시켜준다.
        context.addServletMappingDecoded("/hello", servletName);
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}

```

tomcat9부터는 `tomcat.start()`를 하기 전에 `tomcat.getConnector()`를 해야 실행이 된다.

1. tomcat 객체를 생성
2. servlet 객체를 생성했다.

### 결과

![](picture/hello_tomcat.png)

스프링 부트에서는 위와 같은 설정을 AutoConfiguration에서 자동으로 잡아준다.

> spring-boot-autoconfigure/spring.factories

`ServletWebServerFactoryAutoConfiguration`는 서블릿 웹 서버를 설정해주는 자동 설정이다.
`TomcatServletWebServerFactoryCustomizer`을 통해 톰켓을 커스터 마이징 한다.
