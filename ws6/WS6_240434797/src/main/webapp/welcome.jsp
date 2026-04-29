<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head><title>Welcome</title></head>
<body>
  <jsp:useBean id="userInfo" class="ict.bean.UserInfo" scope="session"/>
  <b>Hello, <jsp:getProperty name="userInfo" property="username"/></b>
  <p>Welcome to the ICT</p>
  <form method="post" action="main">
    <input type="hidden" name="action" value="logout"/>
    <input type="submit" value="Logout"/>
  </form>
  <hr/>
  <a href="brandController?action=list">getAllBrands</a>
</body>
</html>
