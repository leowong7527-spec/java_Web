<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <strong>CCHC Community Clinic</strong>
</div>
<div class="container">
    <div class="card" style="max-width: 560px; margin: 30px auto;">
        <h2>Patient Registration</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/auth/register">
            <label>Username</label>
            <input type="text" name="username" required/>
            <label>Password</label>
            <input type="password" name="password" required/>
            <label>Full Name</label>
            <input type="text" name="fullName" required/>
            <label>Phone</label>
            <input type="text" name="phone"/>
            <label>Email</label>
            <input type="email" name="email"/>
            <button type="submit">Register</button>
        </form>
        <p><a href="<%= request.getContextPath() %>/auth/login">Back to Login</a></p>
    </div>
</div>
</body>
</html>

