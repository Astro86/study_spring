# JAVA config을 이용한 DI

```shell
.
├── ReadMe
├── di-javaconfig.iml
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── sample
│   │   │       ├── ProductSampleRun.java
│   │   │       ├── config
│   │   │       │   └── AppConfig.java
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
    │       │   └── AppConfig.class
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

> src/main/java/sample/di/business/service/ProductService.java

```java
public interface ProductService {
    void addProduct(Product product);
    Product findByProductName(String name);
}
```

> src/main/java/sample/di/business/service/ProdectServiceImpl.java

```java
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
public class ProductDaoImpl implements ProductDao {

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
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        ProductService productService = ctx.getBean(ProductService.class);

        productService.addProduct(new Product("ホチキス", 100));

        Product product = productService.findByProductName("ホチキス");
        System.out.println(product);

    }
}
```

> src/main/java/sample/di/business/domain/Prodect.java

```java

public class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", price=" + price + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + price;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (price != other.price)
            return false;
        return true;
    }

}
```

> src/main/java/sample/config/AppConfig.java

```java
@Configuration
public class AppConfig {
	@Bean(autowire = Autowire.BY_TYPE)
	public ProductServiceImpl productServices() {
		return new ProductServiceImpl();
	}

	@Bean
	public ProductDaoImpl productDao() {
		return new ProductDaoImpl();
	}
}
```
