<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Notification" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) {
        messageType = "success";
    }
    int unreadCount = 0;
    for (Notification notification : notifications) {
        if (!notification.isRead()) {
            unreadCount++;
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications - CCHC Patient Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a class="active" href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="toolbar">
            <div>
                <div class="section-title">Notifications Center</div>
                <h1>Messages and updates</h1>
                <p class="muted">Track booking confirmations, queue updates, and profile messages.</p>
            </div>
            <a class="action-link" href="<%= request.getContextPath() %>/patient/notifications?markRead=true">Mark all as read</a>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="muted">Total notifications</span>
            <span class="value"><%= notifications.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Unread</span>
            <span class="value"><%= unreadCount %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Read</span>
            <span class="value"><%= notifications.size() - unreadCount %></span>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Activity Stream</div>
                <h3>Latest patient notifications</h3>
            </div>
        </div>
        <% if (message != null) { %>
            <div class="alert alert-<%= messageType %>"><%= message %></div>
        <% } %>
        <table>
            <tr><th>Time</th><th>Type</th><th>Status</th><th>Message</th></tr>
            <% if (notifications.isEmpty()) { %>
            <tr><td colspan="4" class="muted">No notifications.</td></tr>
            <% } %>
            <% for (Notification notification : notifications) { %>
            <tr>
                <td><%= DateTimeUtil.format(notification.getCreatedAt()) %></td>
                <td><ui:statusBadge value="<%= notification.getType() %>" /></td>
                <td><ui:statusBadge value="<%= notification.isRead() ? \"Read\" : \"Unread\" %>" /></td>
                <td><%= notification.getMessage() %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

