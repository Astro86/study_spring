# template 만들기

> list

```html
<!DOCTYPE html>
<html lang="ko"
    xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Board Form</title>
    <link rel="stylesheet" th:href="@{/css/base.css}" />
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}" />
</head>
<body>
<div th:replace="layout/header::header"></div>

<div class="container">
    <div class="page-header">
        <h1>게시글 목록</h1>
    </div>
    <div class="pull-right" style="width:100px;margin:10px 0;">
        <a href="/board" class="btn btn-primary btn-block">등록</a>
    </div>
    <br/><br/><br/>
    <div id="mainHide">
        <table class="table table-hover">
            <thead>
            <tr>
                <th class="col-md-1">#</th>
                <th class="col-md-2">서비스 분류</th>
                <th class="col-md-5">제목</th>
                <th class="col-md-2">작성 날짜</th>
                <th class="col-md-2">수정 날짜</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="board : ${boardList}">
                <td th:text="${board.idx}"></td>
                <td th:text="${board.boardType.value}"></td>
                <td><a th:href="'/board?idx='+${board.idx}" th:text="${board.title}"></a></td>
                <td th:text="${board.createdDate} ? ${#temporals.format(board.createdDate,'yyyy-MM-dd HH:mm')} : ${board.createdDate}"></td>
                <td th:text="${board.updatedDate} ? ${#temporals.format(board.updatedDate,'yyyy-MM-dd HH:mm')} : ${board.updatedDate}"></td>
            </tr>
            </tbody>
        </table>
    </div>
    <br/>
    <!-- Pagination -->
    <nav aria-label="Page navigation" style="text-align:center;">
        <ul class="pagination" th:with="startNumber=${T(Math).floor(boardList.number/10)}*10+1, endNumber=(${boardList.totalPages} > ${startNumber}+9) ? ${startNumber}+9 : ${boardList.totalPages}">
            <li><a aria-label="Previous" href="/board/list?page=1">&laquo;</a></li>
            <li th:style="${boardList.first} ? 'display:none'">
                <a th:href="@{/board/list(page=${boardList.number})}">&lsaquo;</a>
            </li>

            <li th:each="page :${#numbers.sequence(startNumber, endNumber)}" th:class="(${page} == ${boardList.number}+1) ? 'active'">
                <a th:href="@{/board/list(page=${page})}" th:text="${page}"><span class="sr-only"></span></a>
            </li>

            <li th:style="${boardList.last} ? 'display:none'">
                <a th:href="@{/board/list(page=${boardList.number}+2)}">&rsaquo;</a>
            </li>
            <li><a aria-label="Next" th:href="@{/board/list(page=${boardList.totalPages})}">&raquo;</a></li>
        </ul>
    </nav>
    <!-- /Pagination -->
</div>

<div th:replace="layout/footer::footer"></div>
</body>
</html>
```

> form

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Board Form</title>
    <link rel="stylesheet" th:href="@{/css/base.css}"/>
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}"/>
</head>
<body>
<div th:replace="layout/header::header"></div>

<div class="container">
    <div class="page-header">
        <h1>게시글 등록</h1>
    </div>
    <br/>
    <input id="board_idx" type="hidden" th:value="${board?.idx}"/>
    <input id="board_create_date" type="hidden" th:value="${board?.createdDate}"/>
    <table class="table">
        <tr>
            <th style="padding:13px 0 0 15px">게시판 선택</th>
            <td>
                <div class="pull-left">
                    <select class="form-control input-sm" id="board_type">
                        <option>--분류--</option>
                        <option th:value="notice" th:selected="${board?.boardType?.name() == 'notice'}">공지사항</option>
                        <option th:value="free" th:selected="${board?.boardType?.name() == 'free'}">자유게시판</option>
                    </select>
                </div>
            </td>
        </tr>
        <tr>
            <th style="padding:13px 0 0 15px;">생성날짜</th>
            <td><input type="text" class="col-md-1 form-control input-sm" readonly="readonly"
                       th:value="${board?.createdDate} ? ${#temporals.format(board.createdDate,'yyyy-MM-dd HH:mm')} : ${board?.createdDate}"/>
            </td>
        </tr>
        <tr>
            <th style="padding:13px 0 0 15px;">제목</th>
            <td><input id="board_title" type="text" class="col-md-1 form-control input-sm" th:value="${board?.title}"/>
            </td>
        </tr>
        <tr>
            <th style="padding:13px 0 0 15px;">부제목</th>
            <td><input id="board_sub_title" type="text" class="col-md-1 form-control input-sm"
                       th:value="${board?.subTitle}"/></td>
        </tr>
        <tr>
            <th style="padding:13px 0 0 15px;">내용</th>
            <td><textarea id="board_content" class="col-md-1 form-control input-sm" maxlength="140" rows="7"
                          style="height: 200px;"
                          th:text="${board?.content}"></textarea><span class="help-block"></span>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </table>
    <div class="pull-left">
        <a href="/board/list" class="btn btn-default">목록으로</a>
    </div>
    <div class="pull-right">
        <button th:if="!${board?.idx}" type="button" class="btn btn-primary" id="insert">저장</button>
        <button th:if="${board?.idx}" type="button" class="btn btn-info" id="update">수정</button>
        <button th:if="${board?.idx}" type="button" class="btn btn-danger" id="delete">삭제</button>
    </div>
</div>

<div th:replace="layout/footer::footer"></div>

</body>
</html>
```

> footer

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <!--footer-->
    <div th:fragment="footer">
        <footer class="footer">
            <div class="container">
                <p>Copyright 2017 young891221. All Right Reserved. Designed by ssosso</p>
            </div>
        </footer>
    </div>
    <!--/footer-->
</html>
```

> header

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <!--header-->
    <div th:fragment="header">
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a href="/" class="navbar-brand" style="text-decoration:none;">
                        <img th:src="@{/images/spring_boot_gray.png}" class="img-rounded" style="display:inline-block;height:100%;margin-right:5px" />
                        <span style="vertical-align:middle;">SpringBoot Community Web</span>
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="/logout">LOGOUT</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    <!--/header-->
</html>
```