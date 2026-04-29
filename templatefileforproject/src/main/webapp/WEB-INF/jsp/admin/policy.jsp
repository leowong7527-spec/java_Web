<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<Map<String, String>> settings = (List<Map<String, String>>) request.getAttribute("settings");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) messageType = "success";

    // Human-friendly labels
    java.util.Map<String, String> labels = new java.util.LinkedHashMap<>();
    labels.put("max_active_bookings_per_patient", "Max Active Bookings per Patient");
    labels.put("cancellation_cutoff_hours", "Cancellation Cutoff (hours before appointment)");
    labels.put("queue_enabled", "Walk-in Queue Enabled (true / false)");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Policy Settings - CCHC Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f0f4f8; }
        .sidebar { min-height: 100vh; background: linear-gradient(180deg, #0d3b66, #1565c0); }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); border-radius: 8px; margin: 2px 8px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: #fff; }
        .sidebar .nav-link i { width: 20px; }
    </style>
</head>
<body>
<div class="d-flex">
    <%@ include file="nav.jsp" %>
    <div class="flex-grow-1 p-4">
        <h4 class="fw-bold mb-1">Policy Settings</h4>
        <p class="text-muted small mb-3">Configure system-wide booking rules, cancellation windows, and queue controls.</p>

        <% if (message != null) { %>
        <div class="alert alert-<%= "success".equals(messageType) ? "success" : ("warning".equals(messageType) ? "warning" : "danger") %> alert-dismissible fade show">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-shield-check text-warning me-1"></i> System Policy Configuration
            </div>
            <div class="card-body">
                <form method="post" action="<%= request.getContextPath() %>/admin/policy">
                    <% if (settings != null && !settings.isEmpty()) {
                        for (Map<String, String> setting : settings) {
                            String key = setting.get("key");
                            String val = setting.get("value");
                            String desc = setting.get("description");
                            String label = labels.getOrDefault(key, key);
                    %>
                    <div class="mb-4">
                        <label class="form-label fw-semibold"><%= label %></label>
                        <input type="text" name="<%= key %>" class="form-control" value="<%= val %>">
                        <div class="form-text text-muted"><%= desc != null ? desc : "" %></div>
                    </div>
                    <% } } %>
                    <button type="submit" class="btn btn-warning fw-semibold">
                        <i class="bi bi-save me-1"></i>Save Policy Settings
                    </button>
                </form>
            </div>
        </div>

        <div class="card border-0 shadow-sm mt-4">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-info-circle text-info me-1"></i> Policy Guide
            </div>
            <div class="card-body">
                <ul class="mb-0 text-muted small">
                    <li><strong>max_active_bookings_per_patient</strong> — Controls how many future (non-cancelled) bookings a patient may hold at one time. Default: 1.</li>
                    <li class="mt-1"><strong>cancellation_cutoff_hours</strong> — Patients may only cancel or reschedule at least this many hours before their appointment slot. Default: 24.</li>
                    <li class="mt-1"><strong>queue_enabled</strong> — Set to <em>true</em> to allow walk-in queue joining system-wide, <em>false</em> to disable it globally.</li>
                </ul>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
