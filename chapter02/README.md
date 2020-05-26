# 스프링 DI

## DI(Dependency Injection)

DI는 말 그대로 의존관계를 주입해 주는 것이다. 쉽게 말해 오브젝트 사이의 의존 관계를 만들어주는 것이다.


<image src = "images/Product.png" width = "400" align = "center">

- ProductSampleRun : 매인 메서드가 있는 클래스
- ProductService : 비즈니스 로직을 실현하는 클래스
- ProdectDao : 데이터베이스 엑세스 오브젝트

흐름을 보면 ProductSampleRun이 ProductService객체를 생성하고 ProductService객체는 ProductDao객체를 생성한다.  
ProductSampleRun은 ProductService에 의존하고 있고, ProductService객체는 ProductDao객체에 의존하고 있다고 말할 수 있다.

> ProductSampleRun

```java
ProductSampleRun{
    ProductService productService = new ProductService();
    ....
}
```

> ProductService

```java
ProductService{
    ProductDao productDao = new ProductDao();
}
```


## DI 컨테이너를 이용하는 경우

<image src = "images/Product_di.png" width = "400" align = "center">

DI 컨테이너를 이용하게 되면 ProductSampleRun이 이용하는 ProductService의 인스턴스와 ProdectService가 새용하는 ProductDao의 인스턴스를 DI가 생성해준다. 그리고 ProdecDao의 인스턴스를 이용하는 ProductService에 의존관계를 주입해준다. 때문에 코드 상에서 해당 객체들을 생성해주는 코드를 작성할 필요가 없다.

> ProductSampleRun

```java
ProductSampleRun{
    ProductService productService;
    ....
}
```

> ProductService

```java
ProductService{
    ProductDao productDao;
}
```

> 쉽게 말해 DI 컨테이너가 해주는 역할은 의존관계를 갖고 있는 객체의 생성과 생성된 객체를 주입해주는 역할을 한다.


## 인터페이스 기반의 컴포넌트화를 실현

<image src = "images/Product_dao_service.png" width = "400" align = "center">

DI를 이용할 때는 원칙적으로 클래스는 인터페이스에 의존하고 실현 클래스에서는 의존하지 않을 필요가 있다. 때문에 ProductService와 ProductDao를 인터페이스로 하고, 구현 클래스는 인터페이스 이름에 Impl을 덧 붙인 것으로 표현한다.

## DI를 사용할 곳

## 어노테이션을 이용한 DI
스프링의 DI는 크게 두가지로 나뉘게 된다. XML파일을 이용해 Bean들을 등록해 주입하는 방법과 어노테이션을 이용해 Bean을 등록해 주입하는 방법이 있다.

### 어노테이션을 이용해 Bean을 등록하는 방법

#### ProductService

> src/main/java/sample/di/business/service/ProdectService.java

```java
public interface ProductService {
    void addProduct(Product product); // 데이터 베이스에 product를 추가해주기 위한 메서드
    Product findByProductName(String name); // 이름으로 해당 Product를 찾기 위한 메서드
}
```

ProdectService 인터페이스에는 두개의 메소드가 정의 되어 있다.

1. Product를 추가하기 위한 addProduct메소드
2. 이름으로 Product를 찾기 위한 findByProductName메소드

#### ProductServiceImpl

> src/main/java/sample/di/business/service/ProdectServiceImpl.java

```java
@Component 
public class ProductServiceImpl implements ProductService {
    @Autowired // PruductDao 객체의 의존성을 주입을 위해
    private ProductDao productDao;

	public void addProduct(Product product) {
		productDao.addProduct(product); 

	}

	public Product findByProductName(String name) {
		return productDao.findByProductName(name);
	}
}
```

#### ProductDao

> src/main/java/sample/di/business/service/ProdectDao.java

```java
public interface ProductDao {
	void addProduct(Product product);
    Product findByProductName(String name);
}
```

#### ProductDaoImpl

> src/main/java/sample/di/dataaccess/ProductDaoImpl.java

```java
@Component
public class ProductDaoImpl implements ProductDao {
	// Dao만으로 간단하게 구현하게 위해서 RDB에 접속은 하지 않습니다.
	// Map은 RDB대신으로 사용
	private Map<String, Product> storage = new HashMap<String, Product>();

    public Product findByProductName(String name) {
        return storage.get(name);
    }

	public void addProduct(Product product) {
		storage.put(product.getName(), product);
	}
}
```

### 어노테이션을 사용한 Bean정의 파일
src/main/java/sample/config/applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="
     http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans.xsd
     http://www.springframework.org/schema/context
     http://www.springframework.org/schema/context/spring-context.xsd">
  <context:annotation-config />
  <context:component-scan base-package="sample.di.business" />
  <context:component-scan base-package="sample.di.dataaccess" />
</beans>
```

클래스를 만든 것만으로는 DI를 할 수 없고, XML로 기술된 Bean 정의 파일도 만들어야 한다. 보통 Bean 정의 파일을 applicationContext.xml로 한다.

> Bean정의 파일에서 사용되는 주요 스키마

| 명칭    | 스키마 파일        | URI                                           | 설명                                                      |
| ------- | ------------------ | --------------------------------------------- | --------------------------------------------------------- |
| Bean    | spring-beans.xsd   | http://www.springframework.org/schema/beans   | Bean 설정                                                 |
| context | spring-context.xsd | http://www.springframework.org/schema/context | Bean검색과 어노테이션 설정                                |
| util    | spring-util.xsd    | http://www.springframework.org/schema/util    | 정의와 프로퍼티 파일을 불러오는 등의 유틸리티   기능 설정 |
| jee     | spring-jee.xsd     | http://www.springframework.org/schema/jee     | JNDI의 lookup 및 EJB의 lookup 설정                        |
| lang    | spring-lang.xsd    | http://www.springframework.org/schema/lang    | 스크립트 언어를 이용할 경우의 설정                        |
| aop     | spring-aop.xsd     | http://www.springframework.org/schema/aop     | AOP 설정                                                  |
| tx      | spring-tx.xsd      | http://www.springframework.org/schema/tx      | 트랜잭션 설정                                             |
| mvc     | spring-mvc.xsd     | http://www.springframework.org/schema/mvc     | Spring MVC 설정                                           |


### @Autowired와 @Component를 구현하기 위한 태그

| 태그                                             | 설명                                                                                                                                                       |
| ------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| <context:annotation-config/>                     | @Autowired, @Resource를 이요할 때의 선언                                                                                                                   |
| <context:component-scan base-pakage="패키지명"/> | @Component, @Service 등의 어노테이션이 설정된 클래스를 읽어 들여,  DI 컨테이너에 등록되고   base-package속성으로 지정한 패키지 아래의 컴포넌트를 검색한다. |




<image src = "images/context_exclude-filter.png" width = "400" align = "center">


<image src = "images/use-default-filters.png" width = "400" align = "center">


<image src = "images/context_include_filter.png" width = "400" align = "center">