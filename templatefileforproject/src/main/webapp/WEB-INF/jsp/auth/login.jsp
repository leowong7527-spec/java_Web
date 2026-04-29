<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>CCHC Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <strong>CCHC Community Clinic</strong>
</div>
<div class="container">
    <div class="card" style="max-width: 480px; margin: 30px auto;">
        <h2>Login</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/auth/login">
            <label>Username</label>
            <input type="text" name="username" required/>
            <label>Password</label>
            <input type="password" name="password" required/>
            <button type="submit">Sign In</button>
        </form>
        <p><a href="<%= request.getContextPath() %>/auth/register">Create Account</a></p>
    </div>
</div>
</body>
</html>

