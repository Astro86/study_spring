# 6장 데이터베이스 처리 마스터하기

## 6-1장 EntityManager를 이용하나 데이터베이스 처리

```java
코드6-1
package com.tuyano.springboot;

import java.io.Serializable;
import java.util.List;

public interface MyDataDao <T> extends Serializable {
	
	public List<T> getAll();
	
}

코드6-2
package com.tuyano.springboot;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class MyDataDaoImpl implements MyDataDao<MyData> {
	private static final long serialVersionUID = 1L;
	
	private EntityManager entityManager;
	
	public MyDataDaoImpl(){
		super();
	}
	public MyDataDaoImpl(EntityManager manager){
		entityManager = manager;
	}
	
	@Override
	public List<MyData> getAll() {
		Query query = entityManager.createQuery("from MyData");
		List<MyData> list = query.getResultList();
		entityManager.close();
		return list;
	}
	
}

코드6-3
package com.tuyano.springboot;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MyDataRepository;

@Controller
public class HeloController {
	
	@Autowired
	MyDataRepository repository;
	
	@PersistenceContext
	EntityManager entityManager; //●
	
	MyDataDaoImpl dao; //●
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","MyData의 예제입니다.");
		Iterable<MyData> list = dao.getAll(); //●
		mav.addObject("datalist", list);
		return mav;
	}

	@PostConstruct
	public void init(){
		dao = new MyDataDaoImpl(entityManager); //●
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
	}

}

코드6-4
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
	<tr><th>ID</th><th>이름</th><th>메일</th><th>연령</th><th>메모(tel)</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
		<td th:text="${obj.mail}"></td>
		<td th:text="${obj.age}"></td>
		<td th:text="${obj.memo}"></td>
	</tr>
	</table>
</body>
</html>

코드6-5
package com.tuyano.springboot;

import java.io.Serializable;
import java.util.List;

public interface MyDataDao<T> extends Serializable {
	
	public List<T> getAll();
	public T findById(long id);
	public List<T> findByName(String name);
}

코드6-6
@Override
public MyData findById(long id) {
	return (MyData)entityManager.createQuery("from MyData where id = " 
		+ id).getSingleResult();
}

@Override
public List<MyData> findByName(String name) {
	return (List<MyData>)entityManager.createQuery("from MyData where name = " 
		+ name).getResultList();
}
```

## 6-2장 JPQL 활용하기

```java
코드6-7
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
		<tr><td>FIND:</td>
		<td><input type="text" name="fstr" size="20" 
			th:value="${value}"/></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th>
		<th>메일</th><th>연령</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.name}"></td>
		<td th:text="${obj.mail}"></td>
		<td th:text="${obj.age}"></td>
	</tr>
	</table>
</body>
</html>

코드6-8
// import javax.servlet.http.HttpServletRequest; を追加

@RequestMapping(value = "/find", method = RequestMethod.GET)
public ModelAndView find(ModelAndView mav) {
	mav.setViewName("find");
	mav.addObject("title","Find Page");
	mav.addObject("msg","MyData의 예제입니다.");
	mav.addObject("value","");
	Iterable<MyData> list = dao.getAll(); //●
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
		List<MyData> list = dao.find(param);
		mav.addObject("datalist", list);
	}
	return mav;
}

코드6-9
public List<T> find(String fstr);

코드6-10
@Override
public List<MyData> find(String fstr){
	List<MyData> list = null;
	String qstr = "from MyData where id = :fstr";
	Query query = entityManager.createQuery(qstr)
		.setParameter("fstr", Long.parseLong(fstr));
	list = query.getResultList();
	return list;
}

코드6-11
@Override
public List<MyData> find(String fstr){
	List<MyData> list = null;
	String qstr = "from MyData where id = :fid or name like :fname or mail like :fmail";
	Long fid = 0L;
	try {
		fid = Long.parseLong(fstr);
	} catch (NumberFormatException e) {
		//e.printStackTrace();
	}
	Query query = entityManager.createQuery(qstr).setParameter("fid", fid)
			.setParameter("fname", "%" + fstr + "%")
			.setParameter("fmail", fstr + "@%");
	list = query.getResultList();
	return list;
}

코드6-12
@Override
public List<MyData> find(String fstr){
	List<MyData> list = null;
	String qstr = "from MyData where id = ?1 or name like ?2 or mail like ?3";
	Long fid = 0L;
	try {
		fid = Long.parseLong(fstr);
	} catch (NumberFormatException e) {
		//e.printStackTrace();
	}
	Query query = entityManager.createQuery(qstr).setParameter(1, fid)
		.setParameter(2, "%" + fstr + "%")
		.setParameter(3, fstr + "@%");
	list = query.getResultList();
	return list;
}

코드6-13
// import javax.persistence.NamedQuery; 추가

@NamedQuery(
	name="findWithName",
	query="from MyData where name like :fname"
)

코드6-14
@NamedQueries (
	@NamedQuery(
		name="findWithName",
		query="from MyData where name like :fname"
	)
)

코드6-15
@Override
public List<MyData> find(String fstr){
	List<MyData> list = null;
	Long fid = 0L;
	try {
		fid = Long.parseLong(fstr);
	} catch (NumberFormatException e) {
		//e.printStackTrace();
	}
	Query query = entityManager
			.createNamedQuery("findWithName")
			.setParameter("fname", "%" + fstr + "%");
	list = query.getResultList();
	return list;
}

코드6-16
package com.tuyano.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tuyano.springboot.MyData;

@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
	@Query("SELECT d FROM MyData d ORDER BY d.name")
	List<MyData> findAllOrderByName();
}

코드6-17
@RequestMapping(value = "/", method = RequestMethod.GET)
public ModelAndView index(ModelAndView mav) {
	mav.setViewName("index");
	mav.addObject("title","Find Page");
	mav.addObject("msg","MyData의 예제입니다.");
	Iterable<MyData> list = repository.findAllOrderByName(); //dao.getAll(); //●
	mav.addObject("datalist", list);
	return mav;
}

코드6-18
@NamedQuery(
	name="findByAge",
	query="from MyData where age > :min and age < :max"
)

코드6-19	MyDataDao에 추가
public List<MyData> findByAge(int min, int max);

코드6-20	MyDataDaoImpl에 추가
@Override
public List<MyData> findByAge(int min, int max) {
	return (List<MyData>)entityManager
		.createNamedQuery("findByAge")
		.setParameter("min", min)
		.setParameter("max", max)
		.getResultList();
}

코드6-21
@Query("from MyData where age > :min and age < :max")
public List<MyData> findByAge(@Param("min") int min, @Param("max") int max);
```

## 6-3장 Criteria API를 사용한 검색

```java
코드6-22
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

코드6-23
@Override
public List<MyData> getAll() {
	List<MyData> list = null;		
	CriteriaBuilder builder = 
			entityManager.getCriteriaBuilder();
	CriteriaQuery<MyData> query = 
			builder.createQuery(MyData.class);
	Root<MyData> root = query.from(MyData.class);
	query.select(root);
	list = (List<MyData>)entityManager
			.createQuery(query)
			.getResultList();
	return list;
}

코드6-24
@Override
public List<MyData> find(String fstr){
	CriteriaBuilder builder = 
			entityManager.getCriteriaBuilder();
	CriteriaQuery<MyData> query = 
		builder.createQuery(MyData.class);
	Root<MyData> root = 
		query.from(MyData.class);
	query.select(root)
		.where(builder.equal(root.get("name"), fstr));
	List<MyData> list = null;
	list = (List<MyData>) entityManager
			.createQuery(query)
			.getResultList();
	return list;
}

코드6-25
@Override
public List<MyData> getAll() {
	List<MyData> list = null;		
	CriteriaBuilder builder = 
			entityManager.getCriteriaBuilder();
	CriteriaQuery<MyData> query = 
			builder.createQuery(MyData.class);
	Root<MyData> root = query.from(MyData.class);
	query.select(root)
			.orderBy(builder.asc(root.get("name")));
	list = (List<MyData>)entityManager
			.createQuery(query)
			.getResultList();
	return list;
}

코드6-26
@Override
public List<MyData> getAll() {
	int offset = 1; // ●추출 위치 지정
	int limit = 2; // ●추출 개수 지정
	List<MyData> list = null;
	CriteriaBuilder builder = 
			entityManager.getCriteriaBuilder();
	CriteriaQuery<MyData> query = 
			builder.createQuery(MyData.class);
	Root<MyData> root = 
			query.from(MyData.class);
	query.select(root);
	list = (List<MyData>)entityManager
			.createQuery(query)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	return list;
}
```

## 6-4장 엔터티 연계

```java
코드6-27――MsgData.java
package com.tuyano.springboot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "msgdata")
public class MsgData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull
	private long id;

	@Column
	private String title;

	@Column(nullable = false)
	@NotEmpty
	private String message;

	@ManyToOne
	private MyData mydata;

	public MsgData() {
		super();
		mydata = new MyData();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MyData getMydata() {
		return mydata;
	}

	public void setMydata(MyData mydata) {
		this.mydata = mydata;
	}
}

코드6-28
// 다음 import를 추가. 다른 package/import는 생략하고 있다
// import javax.persistence.CascadeType;
// import javax.persistence.OneToMany;
// import java.util.List;

@Entity
@Table(name = "mydata")
public class MyData {
	
	@OneToMany(cascade=CascadeType.ALL)
	@Column(nullable = true)
	private List<MsgData> msgdatas;
	
	public List<MsgData> getMsgdatas() {
		return msgdatas;
	}

	public void setMsgdatas(List<MsgData> msgdatas) {
		this.msgdatas = msgdatas;
	}
	
	……이하 생략……	
}

코드6-29――MsgDataRepository.java
package com.tuyano.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tuyano.springboot.MsgData;

public interface MsgDataRepository 
	extends JpaRepository<MsgData, Long> {
	
}

코드6-30――MsgDataDao.java
package com.tuyano.springboot;

import java.io.Serializable;
import java.util.List;

public interface MsgDataDao<T> {
	
	public List<MsgData> getAll();
	public MsgData findById(long id);

}

코드6-31――MsgDataDaoImpl.java
package com.tuyano.springboot;

import java.util.List;

import javax.persistence.EntityManager;

public class MsgDataDaoImpl implements MsgDataDao<MsgDataDao> {

	private EntityManager entityManager;
	
	public MsgDataDaoImpl(){
		super();
	}
	public MsgDataDaoImpl(EntityManager manager){
		entityManager = manager;
	}

	@Override
	public List<MsgData> getAll() {
		return entityManager
				.createQuery("from MsgData")
				.getResultList();
	}

	@Override
	public MsgData findById(long id) {
		return (MsgData)entityManager
				.createQuery("from MsgData where id = " 
				+ id).getSingleResult();
	}
}

코드6-32――showMsgData.html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
	<title th:text="${title}">top page</title>
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
	<h1 th:text="${title}">MyMsg page</h1>
	<p th:text="${msg}"></p>
	<table>
	<form method="post" action="/msg" 
			th:object="${formModel}">
		<input type="hidden" name="id" th:value="*{id}" />
		<tr><td><label for="title">제목</label></td>
			<td><input type="text" name="title" 
				th:value="*{title}" /></td></tr>
		<tr><td><label for="message">메시지</label></td>
			<td><textarea name="message"  
				th:text="*{message}"></textarea></td></tr>
		<tr><td><label for="mydata">MYDATA_ID</label></td>
			<td><input type="text" name="mydata" /></td></tr>
		<tr><td></td><td><input type="submit" /></td></tr>
	</form>
	</table>
	<hr/>
	<table>
	<tr><th>ID</th><th>이름</th><th>제목</th></tr>
	<tr th:each="obj : ${datalist}">
		<td th:text="${obj.id}"></td>
		<td th:text="${obj.mydata.name}"></td>
		<td th:text="${obj.title}"></td>
	</tr>
	</table>
</body>
</html>

코드6-33
package com.tuyano.springboot;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tuyano.springboot.repositories.MsgDataRepository;

@Controller
public class MsgDataController {
	
	@Autowired
	MsgDataRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	MsgDataDaoImpl dao;

	@RequestMapping(value = "/msg", method = RequestMethod.GET)
	public ModelAndView msg(ModelAndView mav) {
		mav.setViewName("showMsgData");
		mav.addObject("title","Sample");
		mav.addObject("msg","MsgData의 예제입니다.");
		MsgData msgdata = new MsgData();
		mav.addObject("formModel", msgdata);
		List<MsgData> list = (List<MsgData>)dao.getAll();
		mav.addObject("datalist", list);
		return mav;
	}

	@RequestMapping(value = "/msg", method = RequestMethod.POST)
	public ModelAndView msgform(
			@Valid @ModelAttribute MsgData msgdata, 
			Errors result, 
			ModelAndView mav) {
		if (result.hasErrors()) {
			mav.setViewName("showMsgData");
			mav.addObject("title", "Sample [ERROR]");
			mav.addObject("msg", "값을 다시 확인해주세요!");
			return mav;
		} else {
			repository.save(msgdata);
			return new ModelAndView("redirect:/msg");
		}
	}

	@PostConstruct
	public void init(){
		System.out.println("ok");
		dao = new MsgDataDaoImpl(entityManager);
	}

}
```