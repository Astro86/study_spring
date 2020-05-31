# 4장 - 템플릿 엔진 마스터하기


## 4-1장 타임리프 마스터하기
```html
<body>
	<h1>Helo page</h1>
	<p th:text="${new java.util.Date().toString()}"></p>
</body>
```


```html
<body>
	<h1>Helo page</h1>
	<p th:text="${#dates.format(new java.util.Date(),'dd/MMM/yyyy HH:mm')}"></p>
	<p th:text="${#numbers.formatInteger(1234,7)}"></p>
	<p th:text="${#strings.toUpperCase('Welcome to Spring.')}"></p>
</body>
```


```java
코드4-3
<body>
	<h1>Helo page</h1>
	<p th:text="'from parameter... id=' + ${param.id[0] + ',name=' + param.name[0]}"></p>
</body>
```


```java
코드 4-4
content.title=message sample page.
content.message=this is sample message from properties.
```

```java
코드4-5
<body>
  <h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="#{content.message}"></p>
</body>


```java
코드4-6
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p><a th:href="@{'/home/' + ${param.id[0]}}">link</a></p>
</body>
```

```java
코드4-7
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("msg","current data.");
	DataObject obj = new DataObject(123, "lee","lee@flower");
	mav.addObject("object",obj);
	return mav;
}
```

```java
코드4-8
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
	<p th:text="${msg}">message.</p>
	<table th:object="${object}">
		<tr><th>ID</th><td th:text="*{id}"></td></tr>
		<tr><th>NAME</th><td th:text="*{name}"></td></tr>
		<tr><th>MAIL</th><td th:text="*{value}"></td></tr>
	</table>
</body>
</html>
```


```java
코드4-9
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<div th:object="${object}">
		<p th:text="|my name is *{name}. mail address is *{value}.|">message.</p>
	</div>
</body>
```


```java
코드4-10
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("msg","message 1<hr/>message 2<br/>message 3");
	return mav;
}
```

```java
코드4-11
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${msg}">message.</p>
</body>
```


```java
코드4-12
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:utext="${msg}">message.</p>
</body>
```

## 4-2장 구문, 인라인, 레이아웃

```java
코드4-13
@RequestMapping("/{id}")
public ModelAndView index(@PathVariable int id,
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("id",id);
	mav.addObject("check",id % 2 == 0);
	mav.addObject("trueVal","Even number!");
	mav.addObject("falseVal","Odd number...");
	return mav;
}
```

```java
코드4-14
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="${id} + ' is ' + (${check} ? ${trueVal} : ${falseVal})"></p>
</body>
```
$(check)의 값이 참일 경우 ${trueVal}값을 거짓일 경우 ${falseVal}을 보여준다.

### 조건 분기의 th:if
```java
@RequestMapping("/{id}")
public ModelAndView index(@PathVariable int id,
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("id",id);
	mav.addObject("check",id >= 0);
	mav.addObject("trueVal","POSITIVE!");
	mav.addObject("falseVal","negative...");
	return mav;
}
```

> th:if/th:unless 사용
```java
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:if="${check}" th:text="${id} + ' is ' + ${trueVal}">message.</p>
	<p th:unless="${check}" th:text="${id} + ' is ' + ${falseVal}">message.</p>
</body>
```

### 조건 분기의 th:switch
```java
@RequestMapping("/{month}")
public ModelAndView index(@PathVariable int month,
		ModelAndView mav) {
	mav.setViewName("index");
	int m = Math.abs(month) % 12;
	m = m == 0 ? 12 : m;
	mav.addObject("month",m);
	mav.addObject("check",Math.floor(m / 3));
	return mav;
}
```

```java
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<div th:switch="${check}">
		<p th:case="0" th:text="|${month} - Winter|"></p>
		<p th:case="1" th:text="|${month} - Spring|"></p>
		<p th:case="2" th:text="|${month} - Summer|"></p>
		<p th:case="3" th:text="|${month} - Autumn|"></p>
		<p th:case="4" th:text="|${month} - Winter|"></p>
		<p th:case="*">...?</p>
	</div>
</body>
```


### 반복의 th:each


```java
코드4-19
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	ArrayList<String[]> data = new ArrayList<String[]>();
	data.add(new String[]{"park","park@yamada","090-999-999"});
	data.add(new String[]{"lee","lee@flower","080-888-888"});
	data.add(new String[]{"choi","choi@happy","080-888-888"});
	mav.addObject("data",data);
	return mav;
}
```
view쪽으로 ArrayList를 이용해 여러데이터를 한번에 보내고 있다.

```java
코드4-20
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<table>
		<tr>
			<th>NAME</th>
			<th>MAIL</th>
			<th>TEL</th>
		</tr>
		<tr th:each="obj:${data}">
			<td th:text="${obj[0]}"></td>
			<td th:text="${obj[1]}"></td>
			<td th:text="${obj[2]}"></td>
		</tr>
	</table>
</body>
```
th:each를 이용해 model로부터 받은 Array데이터를 순회하게 한다.

```java
코드4-21
@RequestMapping("/{num}")
public ModelAndView index(@PathVariable int num,
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("num",num);
	if (num >= 0){
		mav.addObject("check","num >= data.size() ? 0 : num");
	} else {
		mav.addObject("check","num <= data.size() * -1 ? 0 : num * -1");
	}
	ArrayList<DataObject> data = new ArrayList<DataObject>();
	data.add(new DataObject(0,"park","park@yamada"));
	data.add(new DataObject(1,"lee","lee@flower"));
	data.add(new DataObject(2,"choi","choi@happy"));
	mav.addObject("data",data);
	return mav;
}
```

```java
코드4-22
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p th:text="|expression[ ${check} ]|"></p>
	<table th:object="${data.get(__${check}__)}">
		<tr><th>ID</th><td th:text="*{id}"></td></tr>
		<tr><th>NAME</th><td th:text="*{name}"></td></tr>
		<tr><th>MAIL</th><td th:text="*{value}"></td></tr>
	</table>
</body>
```


```java
코드4-23
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	ArrayList<DataObject> data = new ArrayList<DataObject>();
	data.add(new DataObject(0,"park","park@yamada"));
	data.add(new DataObject(1,"lee","lee@flower"));
	data.add(new DataObject(2,"choi","choi@happy"));
	mav.addObject("data",data);
	return mav;
}
```

```java
코드4-24
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<table th:inline="text">
		<tr>
		<th>ID</th>
		<th>NAME</th>
		<th>MAIL</th>
		</tr>
		<tr th:each="obj : ${data}">
		<td>[[${obj.id}]]</td>
		<td>[[${obj.name}]]</td>
		<td>[[${obj.value}]]</td>
		</tr>
	</table>
</body>
```


```java
코드4-25
@RequestMapping("/{tax}")
public ModelAndView index(@PathVariable int tax,
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("tax",tax);
	return mav;
}
```


```java
코드4-26
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	</style>
	<script th:inline="javascript">
	function action(){
		var val = document.getElementById("text1").value;
		var res = parseInt(val * ((100 + /*[[${tax}]]*/) / 100));
		document.getElementById("msg").innerHTML = 
			"include tax: " + res;
	}
	</script>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<p id="msg"></p>
	<input type="text" id="text1" />
	<button onclick="action()">click</button>
</body>
</html>
```


```java
코드4-27
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>part page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	</style>
	<style th:fragment="frag_style">
	div.fragment {
		border:solid 3px lightgray;
		padding:0px 20px;
	}
	</style>
</head>
<body>
	<h1>Part page</h1>
	<div th:fragment="frag_body">
		<h2>fragment</h2>
		<div class="fragment">
			<p>this is fragment content.</p>
			<p>sample message...</p>
		</div>
	</div>
</body>
</html>
```


```java
코드4-28
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title>top page</title>
	<meta http-equiv="Content-Type" 
		content="text/html; charset=UTF-8" />
	<style>
	h1 { font-size:18pt; font-weight:bold; color:gray; }
	body { font-size:13pt; color:gray; margin:5px 25px; }
	</style>
	<style th:include="part :: frag_style"></style>
</head>
<body>
	<h1 th:text="#{content.title}">Helo page</h1>
	<div th:include="part :: frag_body">
		<p>this is orginal content.</p>
	</div>
</body>
</html>
```

```java
코드4-29
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	return mav;
}
```

## 4-3장 기타 템플릿 엔진

```java
코드4-30
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

```java
코드4-31
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
</dependency>
```


```java
코드4-32
spring.mvc.view.prefix: /WEB-INF/jsp/
spring.mvc.view.suffix: .jsp
```

```java
코드4-33
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" 
	contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC 
	"-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" 
	content="text/html; charset=utf-8">
<title>JSP Index Page</title>
</head>
<body>
	<h1>Index page</h1>
	<p>this is JSP sample page.</p>
	<%=new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date()) %>
</body>
</html>
```


```java
코드4-34
@RequestMapping("/")
public String index() {
	return "index";
}
```

```java
코드4-35
<body>
	<h1>Index page</h1>
	<p>${val}</p>
	<form method="post" action="/">
		<input type="text" name="text1">
		<input type="submit">
	</form>
</body>
```


```java
코드4-36
@RequestMapping(value="/", method=RequestMethod.GET)
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("val", "please type...");
	return mav;
}

@RequestMapping(value="/", method=RequestMethod.POST)
public ModelAndView send(@RequestParam String text1,
		ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("val", "you typed: '" + text1 + "'.");
	return mav;
}
```


```java
코드4-37
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-groovy-templates</artifactId>
</dependency>
```


```java
코드4-38
html {
	head {
		title('Groovy Page')
		style('''
			h1 { font-size:18pt; font-weight:bold; color:gray; }
			body { font-size:13pt; color:gray; margin:5px 25px; }			
		''')
	}
	body {
		h1('Index Page')
		p('This is Groovy sample page.')
	}
}
```

```java
코드4-39
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	return mav;
}
```


```java
코드4-40
body {
	h1('Index Page')
	p(msg)
	form(method:'post',action:'/'){
		input(type:'text',name:'num')
		input(type:'submit')
	}
}
```

```java
코드4-41
@RequestMapping(value="/", method=RequestMethod.GET)
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("msg", "type a number...");
	return mav;
}

@RequestMapping(value="/", method=RequestMethod.POST)
public ModelAndView send(@RequestParam int num,
		ModelAndView mav) {
	mav.setViewName("index");
	int total = 0;
	for(int i = 1;i <=num;i++)
		total += i;
	mav.addObject("msg", "total: " + total + " !!");
	return mav;
}
```


```java
코드4-42
@RequestMapping("/")
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("msg","data table.");
	ArrayList<DataObject> data = new ArrayList<DataObject>();
	data.add(new DataObject(0,"park","park@yamada"));
	data.add(new DataObject(1,"lee","lee@flower"));
	data.add(new DataObject(2,"choi","choi@happy"));
	mav.addObject("data",data);
	return mav;
}
```

```java
코드4-43
html {
	head {
		title('Groovy Page')
		style('''
			h1 { font-size:18pt; font-weight:bold; color:gray; }
			body { font-size:13pt; color:gray; margin:5px 25px; }			
			tr { margin:5px; }
			th { padding:5px; color:white; background:darkgray; }
			td { padding:5px; color:black; background:#e0e0ff; }
			''')
	}
	body {
		h1('Index Page')
		p(msg)
		table {
			tr {
				th('ID')
				th('NAME')
				th('MAIL')
			}
			data.each{obj ->
				tr {
					td(obj.id)
					td(obj.name)
					td(obj.value)
				}
			}
		}
	}
}
```