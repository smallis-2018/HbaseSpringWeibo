<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<style>
    html,
    head {
        height: 100%;
    }

    body {
        display: -ms-flexbox;
        display: flex;
        -ms-flex-align: center;
        align-items: center;
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
    }

    .form-signin {
        width: 100%;
        max-width: 330px;
        padding: 15px;
        margin: auto;
    }

    .form-signin input[type="text"] {
        margin-bottom: 10px;
    }
</style>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>微博</title>
</head>
<body class="text-center">
<form class="form-signin" action="${pageContext.request.contextPath}/login" method="get">
    <img class="mb-4 rounded-circle" src="${pageContext.request.contextPath}/static/avatar/rick.svg" width="122"
         height="122" alt="">
    <h1 class="h3 mb-3 font-weight-normal">微&nbsp;&nbsp;博</h1>
    <label for="inputId" class="sr-only">登录帐号</label>
    <input type="text" id="inputId" class="form-control" name="myId" placeholder="请输入登录帐号：" required autofocus>
    <button class="btn btn-lg btn-outline-primary btn-block" type="submit">登&nbsp;&nbsp;录</button>
    <span class="alert-danger">${checkMsg}</span>
</form>
</body>
</html>
