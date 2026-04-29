<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Appointment" %>
<%@ page import="ict.bean.QueueEntry" %>
<%@ page import="ict.bean.Notification" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<Appointment> upcomingAppointments = (List<Appointment>) request.getAttribute("upcomingAppointments");
    List<QueueEntry> todayQueue = (List<QueueEntry>) request.getAttribute("todayQueue");
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Patient Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a class="active" href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="toolbar">
            <div>
                <div class="section-title">Patient Portal</div>
                <h1>Welcome, <%= user.getFullName() %></h1>
            </div>
            <div class="panel-actions">
                <a class="action-link" href="<%= request.getContextPath() %>/patient/appointments">Book appointment</a>
                <a class="action-link" href="<%= request.getContextPath() %>/patient/queue">Join queue</a>
            </div>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="muted">Upcoming appointments</span>
            <span class="value"><%= upcomingAppointments.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Active queue entries</span>
            <span class="value"><%= todayQueue.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Latest notifications</span>
            <span class="value"><%= Math.min(notifications.size(), 5) %></span>
        </div>
    </div>

    <div class="grid-2">
        <div class="panel">
            <div class="panel-head">
                <div>
                    <div class="section-title">Appointments</div>
                    <h3>Upcoming visits</h3>
                </div>
                <a class="action-link" href="<%= request.getContextPath() %>/patient/appointments">Manage</a>
            </div>
            <table>
                <tr><th>ID</th><th>Date/Time</th><th>Status</th></tr>
                <% if (upcomingAppointments.isEmpty()) { %>
                <tr><td colspan="3" class="muted">No upcoming appointment.</td></tr>
                <% } %>
                <% for (Appointment appointment : upcomingAppointments) { %>
                <tr>
                    <td>#<%= appointment.getId() %></td>
                    <td><%= DateTimeUtil.format(appointment.getSlotTime()) %></td>
                    <td><ui:statusBadge value="<%= appointment.getStatus() %>" /></td>
                </tr>
                <% } %>
            </table>
        </div>

        <div class="panel">
            <div class="panel-head">
                <div>
                    <div class="section-title">Queue</div>
                    <h3>Today status</h3>
                </div>
                <a class="action-link" href="<%= request.getContextPath() %>/patient/queue">Open queue</a>
            </div>
            <table>
                <tr><th>Queue #</th><th>Status</th><th>Joined</th></tr>
                <% if (todayQueue.isEmpty()) { %>
                <tr><td colspan="3" class="muted">Not in any queue today.</td></tr>
                <% } %>
                <% for (QueueEntry entry : todayQueue) { %>
                <tr>
                    <td><%= entry.getQueueNumber() %></td>
                    <td><ui:statusBadge value="<%= entry.getStatus() %>" /></td>
                    <td><%= DateTimeUtil.format(entry.getJoinedAt()) %></td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Notifications</div>
                <h3>Recent updates</h3>
            </div>
            <a class="action-link" href="<%= request.getContextPath() %>/patient/notifications">View all</a>
        </div>
        <table>
            <tr><th>Time</th><th>Type</th><th>Message</th></tr>
            <% if (notifications.isEmpty()) { %>
            <tr><td colspan="3" class="muted">No notifications yet.</td></tr>
            <% } %>
            <% for (int i = 0; i < notifications.size() && i < 5; i++) {
                Notification notification = notifications.get(i);
            %>
            <tr>
                <td><%= DateTimeUtil.format(notification.getCreatedAt()) %></td>
                <td><ui:statusBadge value="<%= notification.getType() %>" /></td>
                <td><%= notification.getMessage() %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

