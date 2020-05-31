# 7장 스프링 부트 제대로 활용하기

## 7-1장 서비스와 컴포넌트

```java
코드7-1
package com.tuyano.springboot;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

@Service
public class MyDataService {

	@PersistenceContext
	private EntityManager entityManager;

	public List<MyData> getAll() {
		return (List<MyData>) entityManager.createQuery("from MyData").getResultList();
	}

	public MyData get(int num) {
		return (MyData)entityManager
				.createQuery("from MyData where id = " + num)
				.getSingleResult();
	}

	public List<MyData> find(String fstr) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MyData> query = builder.createQuery(MyData.class);
		Root<MyData> root = query.from(MyData.class);
		query.select(root).where(builder.equal(root.get("name"), fstr));
		List<MyData> list = null;
		list = (List<MyData>) entityManager.createQuery(query).getResultList();
		return list;
	}
}
```

```java
코드7-2
package com.tuyano.springboot;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataRepository;

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@Autowired
	private MyDataService service;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title","Find Page");
		mav.addObject("msg","MyData의 예제입니다.");
		List<MyData> list = service.getAll(); //●
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ModelAndView find(ModelAndView mav) {
		mav.setViewName("find");
		mav.addObject("title","Find Page");
		mav.addObject("msg","MyData의 예제입니다.");
		mav.addObject("value","");
		List<MyData> list = service.getAll(); //●
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request,
			ModelAndView mav) {
		mav.setViewName("find");
		String param = request.getParameter("fstr");
		if (param == ""){
			mav = new ModelAndView("redirect:/find");
		} else {
			mav.addObject("title","Find result");
			mav.addObject("msg","「" + param + "」의 검색 결과");
			mav.addObject("value",param);
			List<MyData> list = service.find(param); //●
			mav.addObject("datalist", list);
		}
		return mav;
	}

	@PostConstruct
	public void init(){
		MyData d1 = new MyData();
		d1.setName("tuyano");
		d1.setAge(123);
		d1.setMail("kim@gilbut.co.kr");
		d1.setMemo("090999999");
		repository.save(d1);
		MyData d2 = new MyData();
		d2.setName("lee");
		d2.setAge(15);
		d2.setMail("lee@flower");
		d2.setMemo("080888888");
		repository.save(d2);
		MyData d3 = new MyData();
		d3.setName("choi");
		d3.setAge(37);
		d3.setMail("choi@happy");
		d3.setMemo("070777777");
		repository.save(d3);
		……필요한 만큼 테스트용 데이터 준비……
	}
}
```


```java
코드7-3
package com.tuyano.springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyDataRestController {

	@Autowired
	private MyDataService service;

	@RequestMapping("/rest")
	public List<MyData> restAll() {
		return service.getAll();
	}

	@RequestMapping("/rest/{num}")
	public MyData restBy(@PathVariable int num) {
		return service.get(num);
	}
}
```

```java
코드7-4
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<script src="http://code.jquery.com/jquery.min.js"></script>
	<script th:inline="javascript">
	$(document).ready(function(){
		var num = [[${param.id[0]}]];
		$.get("/rest/" + num, null, callback);
	});
	function callback(result){
		$('#obj').append('<li>id: ' + result.id + '</li>');
		$('#obj').append('<li>name: ' + result.name + '</li>');
		$('#obj').append('<li>mail: ' + result.mail + '</li>');
		$('#obj').append('<li>age: ' + result.age + '</li>');
		$('#obj').append('<li>memo: ' + result.memo + '</li>');
	}
	</script>
	<style>
	……생략……
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p  th:text="${msg}"></p>
	<ol id="obj"></ol>
	<table>
	……생략……
	</table>
</body>
</html>
```


```java
코드7-5
<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

```java
코드7-6
package com.tuyano.springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class MySampleBean {
	private int counter = 0;
	private int max = 10;

	@Autowired
	public MySampleBean(ApplicationArguments args) {
		List<String> files = args.getNonOptionArgs();
		try {
			max = Integer.parseInt(files.get(0));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public int count() {
		counter++;
		counter = counter > max ? 0 : counter;
		return counter;
	}
}
```


```java
코드7-7
public static void main(String[] args) {
	SpringApplication.run(
			MyBootAppApplication.class, 
			new String[]{"100"});
}
```


```java
코드7-8
@Autowired
MySampleBean bean;

@RequestMapping("/count")
public int count() {
	return bean.count();
}
```



## 7-2장 기억해두면 좋은 기타 기능들

```java
코드7-9
package com.tuyano.springboot;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBootAppConfig {
}
```


```java
코드7-10
package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;

import com.tuyano.springboot.repositories.MyDataRepository;

public class MyDataBean {
	
	@Autowired
	MyDataRepository repository;
	
	public String getTableTagById(Long id){
		MyData data = repository.findOne(id);
		String result = "<tr><td>" + data.getName()
				+ "</td><td>" + data.getMail() + 
				"</td><td>" + data.getAge() + 
				"</td><td>" + data.getMemo() + 
				"</td></tr>";
		return result;
	}
}
```


```java
코드7-11
package com.tuyano.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBootAppConfig {

	@Bean
	MyDataBean myDataBean(){
		return new MyDataBean();
	}
}
```

```java
코드7-12
@Autowired
MyDataBean myDataBean;

@RequestMapping(value = "/{id}", method = RequestMethod.GET)
public ModelAndView indexById(@PathVariable long id,
		ModelAndView mav) {
	mav.setViewName("pickup");
	mav.addObject("title","Pickup Page");
	String table = "<table>"
			+ myDataBean.getTableTagById(id)
			+ "</table>";
	mav.addObject("msg","pickup data id = " + id);
	mav.addObject("data",table);
	return mav;
}
```


```java
코드7-13
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
	<p th:text="${msg}"></p>
	<p th:utext="${data}"></p>
</body>
</html>
```

```java
코드7-14
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
@Autowired
MyDataRepository repository;

private static final int PAGE_SIZE = 3; // 한 페이지당 엔터티 수

public Page<MyData> getMyDataInPage(Integer pageNumber) {
	PageRequest pageRequest = new PageRequest(pageNumber - 1, PAGE_SIZE);
	return repository.findAll(pageRequest);
}
```


```java
코드7-15
@RequestMapping(value = "/page/{num}", 
		method = RequestMethod.GET)
public ModelAndView page(@PathVariable Integer num, 
		ModelAndView mav) {
	Page<MyData> page = service.getMyDataInPage(num);
	mav.setViewName("index");
    mav.addObject("title","Find Page");
	mav.addObject("msg","MyData의 예제입니다.");
	mav.addObject("pagenumber", num);
	mav.addObject("datalist", page);
    return mav;
}
```

```java
코드7-16
<table>
<tr><th>ID</th><th>이름</th><th>메일</th><th>연령</th><th>메모(tel)</th></tr>
<tr th:each="obj : ${datalist}">
	<td th:text="${obj.id}"></td>
	<td th:text="${obj.name}"></td>
	<td th:text="${obj.mail}"></td>
	<td th:text="${obj.age}"></td>
	<td th:text="${obj.memo}"></td>
</tr>
</table>
```


```java
코드7-17
package com.tuyano.springboot;

public class MyTLUtility {
	
	public String hello(String name) {
		return "Hello, <b>" + name + "!</b>";
	}
	
	public String prevUrl(int num) {
		return "/page/" + (num > 1 ? num - 1 : 1);
	}
	
	public String nextUrl(int num) {
		return "/page/" + (num + 1);
	}
}
```


```java
코드7-18
package com.tuyano.springboot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

public class MyTLDialect extends AbstractDialect 
		implements IExpressionEnhancingDialect {
	
	private static final Map<String, Object> EXPRESSION_OBJECTS;

	static {
		Map<String, Object> objects = new HashMap<>();
		objects.put("myTLHelper", new MyTLUtility());
		EXPRESSION_OBJECTS = Collections.unmodifiableMap(objects);
	}

	public MyTLDialect() {
		super();
	}
	
	@Override
	public Map<String, Object> getAdditionalExpressionObjects
			(IProcessingContext processingContext) {
		return EXPRESSION_OBJECTS;
	}

	@Override
	public String getPrefix() {
		return null;
	}
}
```


```java
코드 7-19
@Bean
MyTLDialect myTLDialect(){
	return new MyTLDialect();
}
```


```java
코드7-20
<table>
  <tr><th>ID</th><th>이름</th><th>메일</th><th>연령</th><th>메모(tel)</th></tr>
  <tr th:each="obj : ${datalist}">
    <td th:text="${obj.id}"></td>
    <td th:text="${obj.name}"></td>
    <td th:text="${obj.mail}"></td>
    <td th:text="${obj.age}"></td>
    <td th:text="${obj.memo}"></td>
  </tr>
  <tr>
    <td colspan="5">
      <table class="navi">
        <tr>
          <td><div style="text-align: left;">
              <a th:href="${#myTLHelper.prevUrl(pagenumber)}">← Prev</a>
            </div></td>
          <td><div style="text-align: center;">page</div></td>
          <td><div style="text-align: right;">
              <a th:href="${#myTLHelper.nextUrl(pagenumber)}">Next→</a>
            </div></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
```

## 7-3장 MongoDB 사용하기

```java
코드7-21
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

```java
코드7-22
package com.tuyano.springboot;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class MyDataMongo {
	@Id
    private String id;
     
    private String name;
    private String memo;
    private Date date;
    
    public MyDataMongo(String name, String memo) {
    	super();
    	this.name = name;
    	this.memo = memo;
    	this.date = new Date();
    }
    
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getMemo() {
		return memo;
	}
	public Date getDate() {
		return date;
	}
}
```


```java
코드7-23
package com.tuyano.springboot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tuyano.springboot.MyDataMongo;

public interface MyDataMongoRepository 
		extends MongoRepository<MyDataMongo, Long> {

}
```

```java
코드7-24
package com.tuyano.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataMongoRepository;

@Controller
public class HeloController {
	
	@Autowired
	MyDataMongoRepository repository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title","Find Page");
		mav.addObject("msg","MyDataMongo의 예제입니다.");
		Iterable<MyDataMongo> list = repository.findAll();
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@RequestParam("name") String name,
			@RequestParam("memo") String memo,
			ModelAndView mov) {
		MyDataMongo mydata = new MyDataMongo(name,memo);
		repository.save(mydata);
		return new ModelAndView("redirect:/");
	}
}
```


```java
코드7-25
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
	table.navi {width:100%; background:white; }
	table.navi tr { background:white; }
	table.navi tr td { background:white; }
	.err { color:red; }
	</style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form method="post" action="/">
	<tr><td><label for="name">이름</label></td>
		<td><input type="text" name="name" /></td></tr>
	<tr><td><label for="memo">메모</label></td>
	<td><textarea name="memo" 
				cols="20" rows="5"></textarea></td></tr>
	<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>이름</th><th>메모</th><th>일시</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.name}"></td>
		<td th:text="${obj.memo}"></td>
		<td th:text="${obj.date}"></td>
	</tr>
	</table>
</body>
</html>
```

```java
코드7-26
import java.util.List; // 상단 import 부분에 추가
public List<MyDataMongo> findByName(String s);
```

```java
코드7-27
import java.util.List;  // 상단에 추가

@RequestMapping(value = "/find", method = RequestMethod.GET)
public ModelAndView find(ModelAndView mav) {
	mav.setViewName("find");
	mav.addObject("title","Find Page");
	mav.addObject("msg","MyData의 예제입니다.");
	mav.addObject("value","");
	List<MyDataMongo> list = repository.findAll();
	mav.addObject("datalist", list);
	return mav;
}

@RequestMapping(value = "/find", method = RequestMethod.POST)
public ModelAndView search(
		@RequestParam("find") String param,
		ModelAndView mav) {
	mav.setViewName("find");
	if (param == ""){
		mav = new ModelAndView("redirect:/find");
	} else {
		mav.addObject("title","Find result");
		mav.addObject("msg","「" + param + "」의 검색 결과");
		mav.addObject("value",param);
		List<MyDataMongo> list = repository.findByName(param);
		mav.addObject("datalist", list);
	}
	return mav;
}
```


```java
코드7-28
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>find page</title>
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
	<h1 th:text="${title}">find page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form action="/find" method="post">
		<tr><td>검색:</td>
		<td><input type="text" name="find" size="20" 
			th:value="${value}"/></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>이름</th><th>메모</th><th>일시</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.name}"></td>
		<td th:text="${obj.memo}"></td>
		<td th:text="${obj.date}"></td>
	</tr>
	</table>
</body>
</html>
```