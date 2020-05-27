# annotation을 이용한 DI

```shell
.
├── ReadMe
├── di-annotation.iml
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── sample
│   │   │       ├── ProductSampleRun.java
│   │   │       ├── config
│   │   │       │   └── applicationContext.xml
│   │   │       └── di
│   │   │           ├── business
│   │   │           │   ├── domain
│   │   │           │   │   └── Product.java
│   │   │           │   └── service
│   │   │           │       ├── ProductDao.java
│   │   │           │       ├── ProductService.java
│   │   │           │       └── ProductServiceImpl.java
│   │   │           └── dataaccess
│   │   │               └── ProductDaoImpl.java
│   │   └── resources
│   │       └── log4j.xml
│   └── test
│       ├── java
│       └── resources
└── target
    ├── classes
    │   ├── log4j.xml
    │   └── sample
    │       ├── ProductSampleRun.class
    │       ├── config
    │       │   └── applicationContext.xml
    │       └── di
    │           ├── business
    │           │   ├── domain
    │           │   │   └── Product.class
    │           │   └── service
    │           │       ├── ProductDao.class
    │           │       ├── ProductService.class
    │           │       └── ProductServiceImpl.class
    │           └── dataaccess
    │               └── ProductDaoImpl.class
    └── test-classes

24 directories, 19 files
```

> src/main/java/sample/di/business/service/ProdectService.java

```java
public interface ProductService {
    void addProduct(Product product);
    Product findByProductName(String name);
}
```

ProdectService 인터페이스에는 두개의 메소드가 정의 되어 있다.

1. Product를 추가하기 위한 addProduct메소드
2. 이름으로 Product를 찾기 위한 findByProductName메소드

> src/main/java/sample/di/business/service/ProdectServiceImpl.java

```java
@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

	public void addProduct(Product product) {
		productDao.addProduct(product);

	}

	public Product findByProductName(String name) {
		return productDao.findByProductName(name);
	}
}
```

> src/main/java/sample/di/business/service/ProdectDao.java

```java
public interface ProductDao {
	void addProduct(Product product);
    Product findByProductName(String name);
}
```

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

> src/main/java/sample/ProductSampleRun.java

```java
public class ProductSampleRun {

    public static void main(String[] args) {
        ProductSampleRun productSampleRun = new ProductSampleRun();
        productSampleRun.execute();
    }

    @SuppressWarnings("resource")
	public void execute() {
    	// BeanFactory는ApplicationContext에서 변경해도 괜찮습니다
        BeanFactory ctx = new ClassPathXmlApplicationContext(
                "/sample/config/applicationContext.xml");
        ProductService productService = ctx.getBean(ProductService.class);

        productService.addProduct(new Product("공책", 100));

        Product product = productService.findByProductName("공책");
        System.out.println(product);

    }
}
```

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
