<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--获取信息--%>
<c:set var="myId" value="${infoMap.keySet().iterator().next()}"/>
<c:set var="myName" value="${infoMap.get(myId)}"/>
<c:if test="${flag==1}">
    <c:set var="mapName" value="我的关注"/>
</c:if>
<c:if test="${flag==2}">
    <c:set var="mapName" value="关注广场"/>
</c:if>
<c:if test="${flag==3 || flag==4}">
    <c:set var="mapName" value="我的粉丝"/>
</c:if>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>我的主页</title>
</head>
<body class="bg-light">
<div class="container">

    <%--头像名称--%>
    <div class="py-5 text-center">
        <img class="d-block mx-auto mb-4 rounded-circle" src="${pageContext.request.contextPath}/static/avatar/rick.svg"
             alt="" width="122" height="122">
        <h2>${myName}</h2>
    </div>
    <%--头像名称结束--%>


    <div class="row">

        <%--导航列表--%>
        <div class="col-md-4 order-md-2 mb-4">
            <ul class="list-group mb-3">
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/home/getFollow?myId=${myId}">我的关注</a>
                        <small class="text-muted">My Follow</small>
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/home/getFans?myId=${myId}">我的粉丝</a>
                        <small class="text-muted">My fans</small>
                    </div>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed btn btn-light">
                    <div>
                        <a class="text-decoration-none text-dark"
                           href="${pageContext.request.contextPath}/home/getStrange?myId=${myId}">关注广场</a>
                        <small class="text-muted">Find Friends</small>
                    </div>
                </li>
            </ul>

            <form class="card p-2" action="${pageContext.request.contextPath}/home/AreYouAFan" method="post">
                <div class="input-group">
                    <input type="hidden" class="form-control" name="myId" value="${myId}">
                    <input type="text" class="form-control" name="fanName" placeholder="他/她是我的粉丝吗？">
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
                    <%--相互关注列表--%>
                    <c:forEach var="key" items="${followBackMap.keySet()}">
                        <div class="col-md-6">
                            <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
                                <div class="col p-4 d-flex flex-column position-static align-items-center">
                                        <%--名字--%>
                                    <div class="row mb-2">
                                            <%--跳转到其他页面的时候，注意修改id--%>
                                        <a class="text-decoration-none text-dark"
                                           href="${pageContext.request.contextPath}/other/getFans?myId=${key}">
                                            <h3 class="mb-0">${followBackMap.get(key)}</h3>
                                        </a>
                                        <div class="mb-2 d-flex flex-column position-static">
                                            <svg t="1593747250164" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                                 xmlns="http://www.w3.org/2000/svg" p-id="8426" width="20" height="20">
                                                <path d="M790.784 552.96l1.792 0.725333H896l-85.333333 82.858667-0.469334-0.042667-132.949333 132.992-51.498667-51.285333 81.536-81.706667H128v-82.773333l662.016-0.042667 0.768-0.725333zM347.306667 213.333333l51.370666 51.626667L317.013333 346.581333H896v82.858667H234.154667l-0.426667 0.426667-0.981333-0.426667H128l85.333333-82.816 0.64-0.042667L347.306667 213.333333z"
                                                      fill="#1296db" p-id="8427"></path>
                                            </svg>
                                        </div>
                                    </div>

                                    <c:if test="${flag == 1}">
                                        <div class="row mb-2">
                                            <form action="${pageContext.request.contextPath}/home/unfollow/${myId}/${key}">
                                                <button class="btn">
                                                    <svg t="1593451829394" class="icon" viewBox="0 0 1024 1024"
                                                         version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1976"
                                                         width="20" height="20">
                                                        <path d="M512 928.384a25.6 25.6 0 0 1-15.68-5.376c-10.368-8-254.592-197.632-392.512-364.096C51.2 495.424 25.6 432.96 25.6 368a272.768 272.768 0 0 1 272.448-272.448c52.416 0 103.296 14.976 147.2 43.264a25.6 25.6 0 1 1-27.776 43.008 220.16 220.16 0 0 0-119.424-35.072A221.504 221.504 0 0 0 76.8 368c0 52.544 21.76 104.32 66.432 158.272C257.92 664.704 455.232 825.088 512 870.208c56.768-45.12 254.08-205.568 368.768-343.936 44.672-54.016 66.432-105.728 66.432-158.272a221.504 221.504 0 0 0-221.248-221.248 222.016 222.016 0 0 0-191.744 111.232l-32.64 55.232 77.504 51.84a25.664 25.664 0 0 1 5.184 37.952L518.848 478.976l17.984 69.248a25.6 25.6 0 1 1-49.6 12.864L465.92 478.912a25.472 25.472 0 0 1 5.376-23.104l55.296-64.256-73.6-49.28a25.6 25.6 0 0 1-7.808-34.304l44.8-75.776a273.28 273.28 0 0 1 235.968-136.64A272.64 272.64 0 0 1 998.4 367.936c0 64.896-25.6 127.36-78.208 190.912-137.92 166.464-382.208 356.096-392.512 364.096a25.408 25.408 0 0 1-15.616 5.376z"
                                                              p-id="1977"></path>
                                                    </svg>
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                            <c:if test="${flag == 2||flag == 3}">
                                                <div class="row mb-2">
                                                        <%--占位--%>
                                                    <svg t="1593451709125" class="icon" viewBox="0 0 1024 1024"
                                                         version="1.1" xmlns="http://www.w3.org/2000/svg"
                                                         p-id="1142"
                                                         width="46" height="50">
                                                    </svg>
                                                </div>
                                            </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <%--相互关注列表结束--%>

                    <c:forEach var="key" items="${map.keySet()}">
                        <div class="col-md-6">
                            <div class="row no-gutters border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
                                <div class="col p-4 d-flex flex-column position-static align-items-center">
                                        <%--名字--%>
                                    <div class="row mb-2">
                                            <%--跳转到其他页面的时候，注意修改id--%>
                                        <a class="text-decoration-none text-dark"
                                           href="${pageContext.request.contextPath}/other/getFans?myId=${key}">
                                            <h3 class="mb-0">${map.get(key)}</h3>
                                        </a>
                                    </div>


                                                <%--关注/取消关注--%>
                                    <c:if test="${flag == 1}">
                                        <div class="row mb-2">
                                            <form action="${pageContext.request.contextPath}/home/unfollow/${myId}/${key}">
                                                <button class="btn">
                                                    <svg t="1593451829394" class="icon" viewBox="0 0 1024 1024"
                                                         version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1976"
                                                         width="20" height="20">
                                                        <path d="M512 928.384a25.6 25.6 0 0 1-15.68-5.376c-10.368-8-254.592-197.632-392.512-364.096C51.2 495.424 25.6 432.96 25.6 368a272.768 272.768 0 0 1 272.448-272.448c52.416 0 103.296 14.976 147.2 43.264a25.6 25.6 0 1 1-27.776 43.008 220.16 220.16 0 0 0-119.424-35.072A221.504 221.504 0 0 0 76.8 368c0 52.544 21.76 104.32 66.432 158.272C257.92 664.704 455.232 825.088 512 870.208c56.768-45.12 254.08-205.568 368.768-343.936 44.672-54.016 66.432-105.728 66.432-158.272a221.504 221.504 0 0 0-221.248-221.248 222.016 222.016 0 0 0-191.744 111.232l-32.64 55.232 77.504 51.84a25.664 25.664 0 0 1 5.184 37.952L518.848 478.976l17.984 69.248a25.6 25.6 0 1 1-49.6 12.864L465.92 478.912a25.472 25.472 0 0 1 5.376-23.104l55.296-64.256-73.6-49.28a25.6 25.6 0 0 1-7.808-34.304l44.8-75.776a273.28 273.28 0 0 1 235.968-136.64A272.64 272.64 0 0 1 998.4 367.936c0 64.896-25.6 127.36-78.208 190.912-137.92 166.464-382.208 356.096-392.512 364.096a25.408 25.408 0 0 1-15.616 5.376z"
                                                              p-id="1977"></path>
                                                    </svg>
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                            <c:if test="${flag == 2||flag == 3}">
                                                <div class="row mb-2">
                                                    <form action="${pageContext.request.contextPath}/home/follow/${myId}/${key}/${flag}">
                                                        <button class="btn" id="ab">
                                                            <svg t="1593451709125" class="icon" viewBox="0 0 1024 1024"
                                                                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                                                                 p-id="1142"
                                                                 width="20" height="20">
                                                                <path d="M512 928c-28.928 0-57.92-12.672-86.624-41.376L106.272 564C68.064 516.352 32 471.328 32 384c0-141.152 114.848-256 256-256 53.088 0 104 16.096 147.296 46.592 14.432 10.176 17.92 30.144 7.712 44.608-10.176 14.432-30.08 17.92-44.608 7.712C366.016 204.064 327.808 192 288 192c-105.888 0-192 86.112-192 192 0 61.408 20.288 90.112 59.168 138.688l315.584 318.816C486.72 857.472 499.616 863.808 512 864c12.704 0.192 24.928-6.176 41.376-22.624l316.672-319.904C896.064 493.28 928 445.696 928 384c0-105.888-86.112-192-192-192-48.064 0-94.08 17.856-129.536 50.272l-134.08 134.112c-12.512 12.512-32.736 12.512-45.248 0s-12.512-32.736 0-45.248L562.24 196c48.32-44.192 109.664-68 173.76-68 141.152 0 256 114.848 256 256 0 82.368-41.152 144.288-75.68 181.696l-317.568 320.8C569.952 915.328 540.96 928 512 928z"
                                                                      p-id="1143" fill="#d81e06"></path>
                                                            </svg>
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:if>
                                                <%--关注/取消关注结束--%>

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
