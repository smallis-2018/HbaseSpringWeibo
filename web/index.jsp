<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>微博</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/login" method="get">
  <label>登录ID</label>
  <p><input name="myId" type="text"/></p>
  <button type="submit">登录</button>
</form>
<span>${checkMsg}</span>
</body>
</html>
