<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.QueueEntry" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<ClinicService> services = (List<ClinicService>) request.getAttribute("services");
    List<QueueEntry> myQueue = (List<QueueEntry>) request.getAttribute("myQueue");
    Map<Integer, Integer> waitEstimate = (Map<Integer, Integer>) request.getAttribute("waitEstimate");
    String selectedServiceId = (String) request.getAttribute("selectedServiceId");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) {
        messageType = "success";
    }
    Map<Integer, ClinicService> serviceMap = new HashMap<>();
    for (ClinicService service : services) {
        serviceMap.put(service.getId(), service);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Queue - CCHC Patient Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a class="active" href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="toolbar">
            <div>
                <div class="section-title">Queue Management</div>
                <h1>Same-day walk-in queue</h1>
                <p class="muted">Join queue with one click and track status updates in near real-time.</p>
            </div>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="muted">Active entries today</span>
            <span class="value"><%= myQueue.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Services available</span>
            <span class="value"><%= services.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Estimated total wait</span>
            <%
                int totalWait = 0;
                for (QueueEntry e : myQueue) {
                    Integer wait = waitEstimate.get(e.getId());
                    if (wait != null) {
                        totalWait += wait;
                    }
                }
            %>
            <span class="value"><%= totalWait %>m</span>
        </div>
    </div>

    <div class="panel" id="join-panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Join Queue</div>
                <h3>Choose service and get your queue number</h3>
            </div>
        </div>
        <% if (message != null) { %>
            <div class="alert alert-<%= messageType %>"><%= message %></div>
        <% } %>
        <p class="muted" style="margin-top:0;">Queue opens at each clinic opening time and closes one hour before that clinic shuts for the day.</p>
        <form method="post" action="<%= request.getContextPath() %>/patient/queue">
            <input type="hidden" name="action" value="join"/>
            <label>Select Service to Join Queue</label>
            <select name="serviceId" required>
                <% for (ClinicService service : services) { %>
                    <option value="<%= service.getId() %>" <%= String.valueOf(service.getId()).equals(selectedServiceId) ? "selected" : "" %>><%= service.getClinicName() %> - <%= service.getServiceName() %> (<%= service.getOpeningTime() %>-<%= service.getClosingTime() %>)</option>
                <% } %>
            </select>
            <button type="submit">Join Queue</button>
        </form>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Live Status</div>
                <h3>Your queue entries today</h3>
            </div>
            <a class="action-link" href="<%= request.getContextPath() %>/patient/queue">Refresh</a>
        </div>
        <table>
            <tr><th>Service</th><th>Queue #</th><th>Status</th><th>Est. Wait</th><th>Joined</th></tr>
            <% if (myQueue.isEmpty()) { %>
            <tr><td colspan="5" class="muted">No queue entry today.</td></tr>
            <% } %>
            <% for (QueueEntry entry : myQueue) {
                ClinicService service = serviceMap.get(entry.getServiceId());
                Integer wait = waitEstimate.get(entry.getId());
            %>
            <tr>
                <td><%= service == null ? "N/A" : service.getServiceName() %></td>
                <td><%= entry.getQueueNumber() %></td>
                <td><ui:statusBadge value="<%= entry.getStatus() %>" /></td>
                <td><%= wait == null ? 0 : wait %> mins</td>
                <td><%= DateTimeUtil.format(entry.getJoinedAt()) %></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

