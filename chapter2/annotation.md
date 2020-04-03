# annotation을 이용한 DI

> src/main/java/sample/di/business/service/ProdectService.java

```java
public interface ProductService {
    void addProduct(Product product);
    Product findByProductName(String name);
}
```

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
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="
     http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="productService" class="sample.di.business.service.ProductServiceImpl"
    autowire="byType" />
  <bean id="productDao" class="sample.di.dataaccess.ProductDaoImpl" />

</beans>
```
