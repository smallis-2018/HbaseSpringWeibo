<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="myId" value="${infoMap.keySet().iterator().next()}"/>
<c:set var="myName" value="${infoMap.get(myId)}"/>
<c:set var="oid" value="${sessionScope.get('oid')}"/>
<c:if test="${flag==1}">
    <c:set var="mapName" value="他/她的关注"/>
</c:if>
<c:if test="${flag==3}">
    <c:set var="mapName" value="他/她的粉丝"/>
</c:if>

<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>${myName}的主页</title>
</head>
<body class="bg-light">
<div class="container">
    <%--头像名称--%>
    <div class="py-5 text-center">
        <img class="d-block mx-auto mb-4 rounded-circle"
             src="${pageContext.request.contextPath}/static/avatar/rick2.svg"
             alt="" width="122" height="122">
        <h2>${myName}</h2>
    </div>
    <%--头像名称结束--%>


    <div class="row">

        <%--导航列表--%>
        <div class="col-md-4 order-md-2 mb-4">
            <ul class="list-group mb-3">
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light mb-md-3">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/home/getFans?myId=${oid}">返回我的主页</a>
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/other/getFollow?myId=${myId}">他/她的关注</a>
                        <small class="text-muted">His/Her Follow</small>
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/other/getFans?myId=${myId}">他/她的粉丝</a>
                        <small class="text-muted">His/Her fans</small>
                    </div>
                </li>
            </ul>

            <form class="card p-2" action="${pageContext.request.contextPath}/other/AreYouAFan" method="post">
                <div class="input-group">
                    <input type="hidden" class="form-control" name="myId" value="${myId}">
                    <input type="text" class="form-control" name="fanName" placeholder="他/她还被她/他关注吗？">
                    <div class="input-group-append">
                        <button type="submit" class="btn btn-secondary">查询</button>
                    </div>
                </div>
            </form>
        </div>
            <%--导航列表结束--%>


            <%--用户列表--%>
            <div class="col-md-8 order-md-1" id="mapList">
                <h4 class="mb-3">${mapName}</h4>
                <div class="row mb-2">
                    <c:forEach var="key" items="${map.keySet()}">
                        <div class="col-md-6">
                            <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
                                <div class="col p-4 d-flex flex-column position-static align-items-center">
                                    <div class="row mb-2">
                                        <a class="text-decoration-none text-dark"
                                           href="${pageContext.request.contextPath}/other/getFans?myId=${key}">
                                            <h3 class="mb-0">${map.get(key)}</h3>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <%--用户列表结束--%>
    </div>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.min.js"></script>
</html>
