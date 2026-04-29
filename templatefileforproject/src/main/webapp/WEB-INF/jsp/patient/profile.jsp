<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) {
        messageType = "success";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile - CCHC Patient Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a class="active" href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="toolbar">
            <div>
                <div class="section-title">Profile Management</div>
                <h1>Manage your account securely</h1>
                <p class="muted">Keep your personal details updated and change password regularly.</p>
            </div>
            <a class="action-link" href="<%= request.getContextPath() %>/patient/dashboard">Back to dashboard</a>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="muted">Full name</span>
            <span class="value" style="font-size: 20px;"><%= user.getFullName() == null ? "-" : user.getFullName() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Phone</span>
            <span class="value" style="font-size: 20px;"><%= user.getPhone() == null || user.getPhone().isEmpty() ? "Not set" : user.getPhone() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Email</span>
            <span class="value" style="font-size: 18px;"><%= user.getEmail() == null || user.getEmail().isEmpty() ? "Not set" : user.getEmail() %></span>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Account Updates</div>
                <h3>Update details and credentials</h3>
            </div>
        </div>
        <% if (message != null) { %>
            <div class="alert alert-<%= messageType %>"><%= message %></div>
        <% } %>
    </div>

    <div class="grid-2">
        <div class="panel">
            <h3>Update Personal Details</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/profile">
                <input type="hidden" name="action" value="updateProfile"/>
                <label>Full Name</label>
                <input type="text" name="fullName" value="<%= user.getFullName() == null ? "" : user.getFullName() %>" required/>
                <label>Phone</label>
                <input type="text" name="phone" value="<%= user.getPhone() == null ? "" : user.getPhone() %>"/>
                <label>Email</label>
                <input type="email" name="email" value="<%= user.getEmail() == null ? "" : user.getEmail() %>"/>
                <button type="submit">Save Profile</button>
            </form>
        </div>

        <div class="panel">
            <h3>Change Password</h3>
            <form method="post" action="<%= request.getContextPath() %>/patient/profile">
                <input type="hidden" name="action" value="changePassword"/>
                <label>Current Password</label>
                <input type="password" name="oldPassword" required/>
                <label>New Password</label>
                <input type="password" name="newPassword" required/>
                <button type="submit">Update Password</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>

