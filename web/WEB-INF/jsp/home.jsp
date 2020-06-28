<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>主页</title>
</head>
<body>
<div style="float:right;margin-left: 25px;margin-right: 25px;">
    <p>用户名：
        <c:set var="myId" value="${infoMap.keySet().iterator().next()}"/>
        <c:set var="name" value="${infoMap.get(myId)}"/>
        ${name}
    </p>
</div>

<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>关注列表：</p>
    <ol>
        <c:forEach var="key" items="${followMap.keySet()}">
            <li>${followMap.get(key)}
                <a href="${pageContext.request.contextPath}/home/unfollow/${myId}/${key}">取消关注</a>
            </li>
        </c:forEach>
    </ol>
</div>

<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>粉丝列表：</p>
    <ol>
        <c:forEach var="key" items="${fansMap.keySet()}">
            <li>${fansMap.get(key)}</li>
        </c:forEach>
    </ol>
</div>

<div style="float:left;margin-left: 25px;margin-right: 25px;">
    <p>陌生人列表：</p>
    <ol>
        <c:forEach var="key" items="${strangerMap.keySet()}">
            <li>${strangerMap.get(key)}
                <a href="${pageContext.request.contextPath}/home/follow/${myId}/${key}">关注他/她</a>
            </li>
        </c:forEach>
    </ol>
</div>
</body>
</html>
