# 05장 모델과 데이터베이스

## 05-1장 JPA에 의한 데이터베이스 사용하기

```java
코드5-1
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>org.hsqldb</groupId>
	<artifactId>hsqldb</artifactId>
	<scope>runtime</scope>
</dependency>
```

```java
코드5-2
package com.tuyano.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mydata")
public class MyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private long id;
	
	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 200, nullable = true)
	private String mail;

	@Column(nullable = true)
	private Integer age;
	
	@Column(nullable = true)
	private String memo;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
```

```java
코드5-3
package com.tuyano.springboot.repositories;

public interface MyDataRepository {

}
```

```java
코드5-4
package com.tuyano.springboot.repositories;

import com.tuyano.springboot.MyData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
}
```

```java
코드5-5
package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataRepository;

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;

	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("data",list);
		return mav;
	}
}
```

```java
코드5-6
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	pre { border: solid 3px #ddd; padding: 10px; }
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<pre th:text="${data}"></pre>
</body>
</html>
```

## 엔터티의 CRUD

```java
코드5-7
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	tr { margin:5px; }
	th { padding:5px; color:white; background:darkgray; }
	td { padding:5px; color:black; background:#e0e0ff; }
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<table>
	<form method="post" action="/" th:object="${formModel}">
	<tr><td><label for="name">이름</label></td>
		<td><input type="text" name="name" th:value="*{name}" /></td></tr>
	<tr><td><label for="age">연령</label></td>
		<td><input type="text" name="age"  th:value="*{age}" /></td></tr>
	<tr><td><label for="mail">메일</label></td>
		<td><input type="text" name="mail"  th:value="*{mail}" /></td></tr>
	<tr><td><label for="memo">메모</label></td>
	<td><textarea name="memo"  th:text="*{memo}" 
				cols="20" rows="5"></textarea></td></tr>
	<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
	</tr>
	</table>
</body>
</html>
```

```java
코드5-8
package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataRepository;

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","this is sample content.");
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}
}
```


```java
코드5-9
// import javax.annotation.PostConstruct; 코드 상단에 추가

@PostConstruct
public void init(){
	MyData d1 = new MyData();
	d1.setName("kim");
	d1.setAge(123);
	d1.setMail("kim@gilbut.co.kr");
	d1.setMemo("this is my data!");
	repository.saveAndFlush(d1);
	MyData d2 = new MyData();
	d2.setName("lee");
	d2.setAge(15);
	d2.setMail("lee@flower");
	d2.setMemo("my girl friend.");
	repository.saveAndFlush(d2);
	MyData d3 = new MyData();
	d3.setName("choi");
	d3.setAge(37);
	d3.setMail("choi@happy");
	d3.setMemo("my work friend...");
	repository.saveAndFlush(d3);
}
```


```java
코드5-10
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>edit page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	tr { margin:5px; }
	th { padding:5px; color:white; background:darkgray; }
	td { padding:5px; color:black; background:#e0e0ff; }
	</style>
</head>
<body>
	<h1 th:text="${title}">Edit page</h1>
	<table>
	<form method="post" action="/edit" th:object="${formModel}">
		<input type="hidden" name="id" th:value="*{id}" />
		<tr><td><label for="name">이름</label></td>
			<td><input type="text" name="name" th:value="*{name}" /></td></tr>
		<tr><td><label for="age">연령</label></td>
			<td><input type="text" name="age"  th:value="*{age}" /></td></tr>
		<tr><td><label for="mail">메일</label></td>
			<td><input type="text" name="mail"  th:value="*{mail}" /></td></tr>
		<tr><td><label for="memo">메모</label></td>
			<td><textarea name="memo"  th:text="*{memo}" 
			cols="20" rows="5"></textarea></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
</body>
</html>
```

```java
코드5-11
@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
	public MyData findById(Long name);
}
```


```java
코드5-12
@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
public ModelAndView edit(@ModelAttribute MyData mydata, 
		@PathVariable int id,ModelAndView mav) {
	mav.setViewName("edit");
	mav.addObject("title","edit mydata.");
	MyData data = repository.findById((long)id);
	mav.addObject("formModel",data);
	return mav;
}

@RequestMapping(value = "/edit", method = RequestMethod.POST)
@Transactional(readOnly=false)
public ModelAndView update(@ModelAttribute MyData mydata, 
		ModelAndView mav) {
	repository.saveAndFlush(mydata);
	return new ModelAndView("redirect:/");
}
```


```java
코드5-13
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>edit page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	td { padding:0px 20px; background:#eee;}
	</style>
</head>
<body>
	<h1 th:text="${title}">Edit page</h1>
	<table>
	<form method="post" action="/delete" 
		th:object="${formModel}">
		<input type="hidden" name="id" th:value="*{id}" />
		<tr><td><p th:text="|이름：　*{name}|"></p></td></tr>
		<tr><td><p th:text="|연령：　*{age}|" ></p></td></tr>
		<tr><td><p th:text="*{mail}"></p></td></tr>
		<tr><td><p th:text="*{memo}"></p></td></tr>
		<tr><td><input type="submit" value="delete"/></td></tr>
	</form>
	</table>
</body>
</html>
```

```java
코드5-14
@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
public ModelAndView delete(@PathVariable int id,
		ModelAndView mav) {
	mav.setViewName("delete");
	mav.addObject("title","delete mydata.");
	MyData data = repository.findById((long)id);
	mav.addObject("formModel",data);
	return mav;
}

@RequestMapping(value = "/delete", method = RequestMethod.POST)
@Transactional(readOnly=false)
public ModelAndView remove(@RequestParam long id, 
		ModelAndView mav) {
	repository.delete(id);
	return new ModelAndView("redirect:/");
}
```


```java
코드5-15
package com.tuyano.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuyano.springboot.MyData;

@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
	public MyData findById(Long name);
	public List<MyData> findByNameLike(String name);
	public List<MyData> findByIdIsNotNullOrderByIdDesc();
	public List<MyData> findByAgeGreaterThan(Integer age);
	public List<MyData> findByAgeBetween(Integer age1, Integer age2);
}
```

## 5-3장 엔터티의 유효성 검증

```java
코드5-16
package com.tuyano.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mydata")
public class MyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull	// ●
	private long id;

	@Column(length = 50, nullable = false)
	@NotEmpty	// ●
	private String name;

	@Column(length = 200, nullable = true)
	@Email	// ●
	private String mail;

	@Column(nullable = true)
	@Min(0)	// ●
	@Max(200) // ●
	private Integer age;

	@Column(nullable = true)
	private String memo;

	……접근자는 생략……
}
```

```java
코드5-17
@RequestMapping(value = "/", method = RequestMethod.GET)
public ModelAndView index(
	@ModelAttribute("formModel") MyData mydata, 
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("msg","this is sample content.");
	mav.addObject("formModel",mydata);
	Iterable<MyData> list = repository.findAll();
	mav.addObject("datalist",list);
	return mav;
}
```

```java
@RequestMapping(value = "/", method = RequestMethod.POST)
@Transactional(readOnly=false)
public ModelAndView form(
		@ModelAttribute("formModel") 
		@Validated MyData mydata,
		BindingResult result,
		ModelAndView mov) {
	ModelAndView res = null;
	if (!result.hasErrors()){
		repository.saveAndFlush(mydata);
		res = new ModelAndView("redirect:/");
	} else {
		mov.setViewName("index");
		mov.addObject("msg","sorry, error is occured...");
		Iterable<MyData> list = repository.findAll();
		mov.addObject("datalist",list);
		res = mov;
	}
	return res;
}
```


```java
코드5-18
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	tr { margin:5px; }
	th { padding:5px; color:white; background:darkgray; }
	td { padding:5px; color:black; background:#e0e0ff; }
	.err { color:red; }
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form method="post" action="/" th:object="${formModel}">
		<ul>
			<li th:each="error : ${#fields.detailedErrors()}"
				class="err" th:text="${error.message}" />
		</ul>
		<tr><td><label for="name">이름</label></td>
			<td><input type="text" name="name" 
				th:field="*{name}" /></td></tr>
		<tr><td><label for="age">연령</label></td>
			<td><input type="text" name="age"
				th:field="*{age}" /></td></tr>
		<tr><td><label for="mail">메일</label></td>
			<td><input type="text" name="mail" 
					th:field="*{mail}" /></td></tr>
		<tr><td><label for="memo">메모</label></td>
			<td><textarea name="memo" th:field="*{memo}" 
				cols="20" rows="5" ></textarea></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
	</tr>
	</table>
</body>
</html>
```


```java
코드5-19
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form method="post" action="/" th:object="${formModel}">
		<tr><td><label for="name">이름</label></td>
			<td><input type="text" name="name"
				th:value="*{name}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('name')}" 
				th:errors="*{name}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="age">연령</label></td>
			<td><input type="text" name="age"
				th:value="*{age}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('age')}" 
				th:errors="*{age}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="mail">메일</label></td>
			<td><input type="text" name="mail" 
				th:value="*{mail}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('mail')}" 
				th:errors="*{mail}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="memo">메모</label></td>
			<td><textarea name="memo" th:text="*{memo}" 
				cols="20" rows="5" ></textarea></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
	</tr>
	</table>
</body>
```


```java
코드5-20
@Entity
@Table(name="mydata")
public class MyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull	// ●
	private long id;

	@Column(length = 50, nullable = false)
	@NotEmpty(message="공백 불가")	// ●
	private String name;

	@Column(length = 200, nullable = true)
	@Email(message="메일 주소만")	// ●
	private String mail;

	@Column(nullable = true)
	@Min(value=0,message="0이상")	// ●
	@Max(value=200,message="200이하") // ●
	private Integer age;

	@Column(nullable = true)
	private String memo;

	……이하 생략……
}
```

```java
코드5-21
org.hibernate.validator.constraints.NotBlank.message = 공백은 허용되지 않습니다.
org.hibernate.validator.constraints.NotEmpty.message = 공백은 허용되지 않습니다.
javax.validation.constraints.Max.message = {value} 보다 작은 값을 입력해주세요.
javax.validation.constraints.Min.message = {value} 보다 큰 값을 입력해주세요.
org.hibernate.validator.constraints.Email.message = 메일 주소가 아닙니다.
```

```java
코드5-22
org.hibernate.validator.constraints.NotBlank.message = \u7A7A\u767D\u306F\u4E0D\u53EF\u3067\u3059\u3002
org.hibernate.validator.constraints.NotEmpty.message = \u7A7A\u767D\u306F\u4E0D\u53EF\u3067\u3059\u3002
javax.validation.constraints.Max.message = {value} \u3088\u308A\u5C0F\u3055\u304F\u3057\u3066\u4E0B\u3055\u3044\u3002
javax.validation.constraints.Min.message = {value} \u3088\u308A\u5927\u304D\u304F\u3057\u3066\u4E0B\u3055\u3044\u3002
org.hibernate.validator.constraints.Email.message = \u30E1\u30FC\u30EB\u30A2\u30C9\u30EC\u30B9\u3067\u306F\u3042\u308A\u307E\u305B\u3093\u3002
```

```java
코드5-23
package com.tuyano.springboot;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

	@Override
	public void initialize(Phone phone) {
	}

	@Override
	public boolean isValid(String input, ConstraintValidatorContext cxt) {
		if (input == null) {
			return false;
		}
		return input.matches("[0-9()-]*");
	}

}
```

```java
코드5-24
package com.tuyano.springboot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Phone {

	String message() default "please input a phone number.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
```


```java
코드5-25
@Column(nullable = true)
@Phone
private String memo;
```


```java
코드5-26
<tr><td><label for="memo">메모</label></td>
	<td><textarea name="memo" th:text="*{memo}" 
		th:errorclass="err" cols="20" rows="5" ></textarea>
	<div th:if="${#fields.hasErrors('memo')}" 
		th:errors="*{memo}" th:errorclass="err"></div></td></tr>
```


```java
코드5-27
public void init(){
	MyData d1 = new MyData();
	d1.setName("tuyano");
	d1.setAge(123);
	d1.setMail("kim@gilbut.co.kr");
	d1.setMemo("090-999-999"); // ●
	repository.saveAndFlush(d1);
	MyData d2 = new MyData();
	d2.setName("lee");
	d2.setAge(15);
	d2.setMail("lee@flower");
	d2.setMemo("080-888-888"); // ●
	repository.saveAndFlush(d2);
	MyData d3 = new MyData();
	d3.setName("choi");
	d3.setAge(37);
	d3.setMail("choi@happy");
	d3.setMemo("070-777-777"); // ●
	repository.saveAndFlush(d3);
}
```


```java
코드5-28
public @interface Phone {

	String message() default "please input a phone number.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean onlyNumber() default false;	// ●
	
}
```


```java
코드5-29
public class PhoneValidator implements ConstraintValidator<Phone, String> {
	private boolean onlyNumber = false;

	@Override
	public void initialize(Phone phone) {
		onlyNumber = phone.onlyNumber();
	}

	@Override
	public boolean isValid(String input, ConstraintValidatorContext cxt) {
		if (input == null) {
			return false;
		}
		if (onlyNumber) {
			return input.matches("[0-9]*");
		} else {
			return input.matches("[0-9()-]*");
		}
	}
}
```


```java
코드5-30
@Column(nullable = true)
@Phone(onlyNumber=true)
private String memo;
```


```java
코드5-pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.tuyano.springboot</groupId>
	<artifactId>MyBootApp</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>MyBootApp</name>
	<description>sample project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.4.2.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
<groupId>org.hsqldb</groupId>
	<artifactId>hsqldb</artifactId>
	<scope>runtime</scope>
</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
			</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```


```java
코드5-heloController.java
package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataRepository;
import javax.annotation.PostConstruct; 

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(
		@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","this is sample content.");
		mav.addObject("formModel",mydata);
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel") 
			@Validated MyData mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("index");
			mov.addObject("msg","sorry, error is occured...");
			Iterable<MyData> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		return res;
	}
	

	@PostConstruct
	public void init(){
		MyData d1 = new MyData();
		d1.setName("tuyano");
		d1.setAge(123);
		d1.setMail("kim@gilbut.co.kr");
		d1.setMemo("090999999"); // ●
		repository.saveAndFlush(d1);
		MyData d2 = new MyData();
		d2.setName("lee");
		d2.setAge(15);
		d2.setMail("lee@flower");
		d2.setMemo("080888888"); // ●
		repository.saveAndFlush(d2);
		MyData d3 = new MyData();
		d3.setName("choi");
		d3.setAge(37);
		d3.setMail("choi@happy");
		d3.setMemo("070777777"); // ●
		repository.saveAndFlush(d3);
	}

	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute MyData mydata, 
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","edit mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel",data);
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(@ModelAttribute MyData mydata, 
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","delete mydata.");
		MyData data = repository.findById((long)id);
		mav.addObject("formModel",data);
		return mav;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(@RequestParam long id, 
			ModelAndView mav) {
		repository.delete(id);
		return new ModelAndView("redirect:/");
	}
}
```


```java
코드5-MyData.java
package com.tuyano.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mydata")
public class MyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull	
	private long id;

	@Column(length = 50, nullable = false)
	@NotEmpty
	private String name;

	@Column(length = 200, nullable = true)
	@Email
	private String mail;

	@Column(nullable = true)
	@Min(value=0)	
	@Max(value=200) 
	private Integer age;


	@Column(nullable = true)
	@Phone(onlyNumber=true)
	private String memo;


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
```


```java
코드5-Phone.java
package com.tuyano.springboot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Phone {

	String message() default "please input a phone number.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean onlyNumber() default false;	// ●
	
}
```


```java
코드5-PhoneValidator.java
package com.tuyano.springboot;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class PhoneValidator implements ConstraintValidator<Phone, String> {
	private boolean onlyNumber = false;

	@Override
	public void initialize(Phone phone) {
		onlyNumber = phone.onlyNumber();
	}

	@Override
	public boolean isValid(String input, ConstraintValidatorContext cxt) {
		if (input == null) {
			return false;
		}
		if (onlyNumber) {
			return input.matches("[0-9]*");
		} else {
			return input.matches("[0-9()-]*");
		}
	}
}
```


```java
코드5-MyDataRepository.java
package com.tuyano.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuyano.springboot.MyData;

@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
	public MyData findById(Long name);
	public List<MyData> findByNameLike(String name);
	public List<MyData> findByIdIsNotNullOrderByIdDesc();
	public List<MyData> findByAgeGreaterThan(Integer age);
	public List<MyData> findByAgeBetween(Integer age1, Integer age2);
}
```

```java
코드5-index.html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	tr { margin:5px; }
	th { padding:5px; color:white; background:darkgray; }
	td { padding:5px; color:black; background:#e0e0ff; }
	.err { color:red; }
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form method="post" action="/" th:object="${formModel}">
		<tr><td><label for="name">이름</label></td>
			<td><input type="text" name="name"
				th:value="*{name}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('name')}" 
				th:errors="*{name}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="age">연령</label></td>
			<td><input type="text" name="age"
				th:value="*{age}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('age')}" 
				th:errors="*{age}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="mail">메일</label></td>
			<td><input type="text" name="mail" 
				th:value="*{mail}" th:errorclass="err" />
			<div th:if="${#fields.hasErrors('mail')}" 
				th:errors="*{mail}" th:errorclass="err">
				</div></td></tr>
		<tr><td><label for="memo">메모</label></td>
	<td><textarea name="memo" th:text="*{memo}" 
		th:errorclass="err" cols="20" rows="5" ></textarea>
	<div th:if="${#fields.hasErrors('memo')}" 
		th:errors="*{memo}" th:errorclass="err"></div></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
	</tr>
	</table>
</body>
</html>
```


```java
코드5-edit.html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>edit page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	tr { margin:5px; }
	th { padding:5px; color:white; background:darkgray; }
	td { padding:5px; color:black; background:#e0e0ff; }
	</style>
</head>
<body>
	<h1 th:text="${title}">Edit page</h1>
	<table>
	<form method="post" action="/edit" th:object="${formModel}">
		<input type="hidden" name="id" th:value="*{id}" />
		<tr><td><label for="name">이름</label></td>
			<td><input type="text" name="name" th:value="*{name}" /></td></tr>
		<tr><td><label for="age">연령</label></td>
			<td><input type="text" name="age"  th:value="*{age}" /></td></tr>
		<tr><td><label for="mail">메일</label></td>
			<td><input type="text" name="mail"  th:value="*{mail}" /></td></tr>
		<tr><td><label for="memo">메모</label></td>
			<td><textarea name="memo"  th:text="*{memo}" 
			cols="20" rows="5"></textarea></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
</body>
</html>
```


```java
코드5-delete.html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>edit page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	td { padding:0px 20px; background:#eee;}
	</style>
</head>
<body>
	<h1 th:text="${title}">Edit page</h1>
	<table>
	<form method="post" action="/delete" 
		th:object="${formModel}">
		<input type="hidden" name="id" th:value="*{id}" />
		<tr><td><p th:text="|이름：　*{name}|"></p></td></tr>
		<tr><td><p th:text="|연령：　*{age}|" ></p></td></tr>
		<tr><td><p th:text="*{mail}"></p></td></tr>
		<tr><td><p th:text="*{memo}"></p></td></tr>
		<tr><td><input type="submit" value="delete"/></td></tr>
	</form>
	</table>
</body>
</html>
```