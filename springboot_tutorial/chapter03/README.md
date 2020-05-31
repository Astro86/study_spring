# 3장 자바를 이용한 스프링 부트 개발의 기본

## 3-1장 스프링 부트와 스프링 스타터 프로젝트

```java
코드3-1
package com.tuyano.springboot;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
```

```java
코드3-2
<project xmlns="http://maven.apache.org/POM/4.0.0" 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
    http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.tuyano.springboot</groupId>
  <artifactId>MyBootApp</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>MyBootApp</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

```xml
코드3-3
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">

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
		<version>1.3.0.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
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

## 3-2장 RestController사용하기

```java
코드3-4
package com.tuyano.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBootAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBootAppApplication.class, args);
	}
}
```

```java
코드3-5
package com.tuyano.springboot;

public class HeloController {

}
```


```java
코드3-6
package com.tuyano.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeloController {

	@RequestMapping("/")
	public String index() {
		return "Hello Spring-Boot World!!";
	}
}
```


```java
코드3-7
package com.tuyano.springboot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeloController {

	@RequestMapping("/{num}")
	public String index(@PathVariable int num) {
		int res = 0;
		for(int i = 1;i <= num;i++)
			res += i;
		return "total: " + res;
	}
}
```


```java
코드3-8
package com.tuyano.springboot;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeloController {
	String[] names = {"kim",
			"lee","park",
			"choi","jo"};
	String[] mails = {"kim@tuuyano.com",
			"lee@flower","park@yamda",
			"choi@happy","jo@baseball"};

	@RequestMapping("/{id}")
	public DataObject index(@PathVariable int id) {
		return new DataObject(id,names[id],mails[id]);
	}
	
}

class DataObject {
	private int id;
	private String name;
	private String value;
	
	public DataObject(int id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public int getId() { return id; }

	public void setId(int id) { this.id = id; }

	public String getName() { return name; }

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
```

## 3-3 Controller를 이용한 웹 페이지 작성하기

```java
코드3-9
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

코드3-10
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HeloController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
}
```

```java
코드3-11
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
</head>
<body>
	<h1>Helo page</h1>
	<p class="msg">this is Thymeleaf sample page.</p>
</body>
</html>
```


```java
코드3-12
<body>
	<h1>Helo page</h1>
	<p class="msg" th:text="${msg}"></p>
</body>
```


```java
코드3-13
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HeloController {
	
	@RequestMapping("/{num}")
	public String index(@PathVariable int num, Model model) {
		int res = 0;
		for(int i = 1;i <= num;i++)
			res += i;
		model.addAttribute("msg", "total: " + res);
		return "index";
	}
	
}
```

```java
코드3-14
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HeloController {
	
	@RequestMapping("/{num}")
	public ModelAndView index(@PathVariable int num, 
			ModelAndView mav) {
		int res = 0;
		for(int i = 1;i <= num;i++)
			res += i;
		mav.addObject("msg", "total: " + res);
		mav.setViewName("index");
		return mav;
	}
	
}
```


```java
코드3-15
<body>
	<h1>Helo page</h1>
	<p th:text="${msg}">please wait...</p>
	<form method="post" action="/">
		<input type="text" name="text1" th:value="${value}" />
		<input type="submit" value="Click" />
	</form>
</body>
```


```java
코드3-16
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HeloController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","お이름を書いて送信してください。");
		return mav;
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView send(@RequestParam("text1")String str, 
			ModelAndView mav) {
		mav.addObject("msg","こんにちは、" + str + "さん！");
		mav.addObject("value",str);
		mav.setViewName("index");
		return mav;
	}
}
```


```java
코드3-17
<!-- pre { font-size:13pt; color:gray; padding:5px 10px; 
		border:1px solid gray; } を<style>に追加 -->
<body>
	<h1>Helo page</h1>
	<pre th:text="${msg}">please wait...</pre>
	<form method="post" action="/">
		<div>
		<input type="checkbox" id="check1" name="check1" />
		<label for="check1">チェック</label>
		</div>
		<div>
		<input type="radio" id="radioA" name="radio1" value="male" />
		<label for="radioA">남성</label>
		</div>
		<div>
		<input type="radio" id="radioB" name="radio1" value="female" />
		<label for="radioB">여성</label>
		</div>
		<div>
		<select id="select1" name="select1" size="4">
			<option value="Windows">Windows</option>
			<option value="Mac">Mac</option>
			<option value="Linux">Linux</option>
		</select>
		<select id="select2" name="select2" size="4" multiple="multiple">
			<option value="Android">Android</option>
			<option value="iphone">iPhone</option>
			<option value="Winfone">Windows Phone</option>
		</select>
		</div>
		<input type="submit" value="Click" />
	</form>
</body>
```


```java
코드3-18
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HeloController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","フォームを送信下さい。");
		return mav;
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView send(
		@RequestParam(value="check1",required=false)boolean check1,
		@RequestParam(value="radio1",required=false)String radio1,
		@RequestParam(value="select1",required=false)String select1,
		@RequestParam(value="select2",required=false)String[] select2,
		ModelAndView mav) {
		
		String res = "";
		try {
			res = "check:" + check1 +
				" radio:" + radio1 +
				" select:" + select1 + 
				"\nselect2:";
		} catch (NullPointerException e) {}
		try {
			res += select2[0];
			for(int i = 1;i < select2.length;i++)
				res += ", " + select2[i];
		} catch (NullPointerException e) {
			res += "null";
		}
		mav.addObject("msg",res);
		mav.setViewName("index");
		return mav;
	}	
}
```

```java
코드3-19
<body>
	<h1>Helo page</h1>
</body>
```

```java
코드3-20
package com.tuyano.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HeloController {
	
	@RequestMapping("/")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		return mav;
	}
	@RequestMapping("/other")
	public String other() {
		 return "redirect:/";
	}
	
	@RequestMapping("/home")
	public String home() {
		return "forward:/";
	}
}
```