<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head><title>Login</title></head>
<body>
  <form method="post" action="main">
    <input type="hidden" name="action" value="authenticate"/>
    <p>Login with abc/123 or xyz/123 (database validated)</p>
    Username: <input type="text" name="username"/><br/>
    Password: <input type="password" name="password"/><br/>
    <input type="submit" value="Login"/>
  </form>
</body>
</html>
