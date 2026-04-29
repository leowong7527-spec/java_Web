<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> report = (List<Map<String, Object>>) request.getAttribute("report");
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> auditLogs = (List<Map<String, Object>>) request.getAttribute("auditLogs");
    int selectedYear = (Integer) request.getAttribute("selectedYear");
    int selectedMonth = (Integer) request.getAttribute("selectedMonth");

    String[] monthNames = {"", "January","February","March","April","May","June",
                               "July","August","September","October","November","December"};

    // compute totals
    int grandTotal = 0, grandCompleted = 0, grandNoShow = 0, grandCancelled = 0;
    if (report != null) {
        for (Map<String, Object> row : report) {
            grandTotal += (int) row.get("total");
            grandCompleted += (int) row.get("completed");
            grandNoShow += (int) row.get("no_show");
            grandCancelled += (int) row.get("cancelled");
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports - CCHC Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f0f4f8; }
        .sidebar { min-height: 100vh; background: linear-gradient(180deg, #0d3b66, #1565c0); }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); border-radius: 8px; margin: 2px 8px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: #fff; }
        .sidebar .nav-link i { width: 20px; }
        .utilisation-bar { height: 10px; border-radius: 5px; background: #e5e7eb; overflow: hidden; }
        .utilisation-fill { height: 100%; border-radius: 5px; background: linear-gradient(90deg, #0d6efd, #0dcaf0); }
    </style>
</head>
<body>
<div class="d-flex">
    <%@ include file="nav.jsp" %>
    <div class="flex-grow-1 p-4">
        <h4 class="fw-bold mb-1">Reports &amp; Analytics</h4>
        <p class="text-muted small mb-3">Appointment utilisation rates and no-show summaries by clinic and service.</p>

        <!-- Filter -->
        <div class="card border-0 shadow-sm mb-4">
            <div class="card-body">
                <form method="get" action="<%= request.getContextPath() %>/admin/reports" class="row g-3 align-items-end">
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Year</label>
                        <input type="number" name="year" class="form-control form-control-sm"
                               value="<%= selectedYear %>" min="2020" max="2099">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Month</label>
                        <select name="month" class="form-select form-select-sm">
                            <% for (int m = 1; m <= 12; m++) { %>
                            <option value="<%= m %>" <%= m == selectedMonth ? "selected" : "" %>><%= monthNames[m] %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary btn-sm w-100">
                            <i class="bi bi-funnel me-1"></i>Filter
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Summary cards -->
        <div class="row g-3 mb-4">
            <div class="col-sm-3">
                <div class="card border-0 shadow-sm p-3 text-center">
                    <div class="text-muted small">Total Bookings</div>
                    <div class="h4 fw-bold text-primary"><%= grandTotal %></div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="card border-0 shadow-sm p-3 text-center">
                    <div class="text-muted small">Completed</div>
                    <div class="h4 fw-bold text-success"><%= grandCompleted %></div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="card border-0 shadow-sm p-3 text-center">
                    <div class="text-muted small">No-Show</div>
                    <div class="h4 fw-bold text-warning"><%= grandNoShow %></div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="card border-0 shadow-sm p-3 text-center">
                    <div class="text-muted small">Cancelled</div>
                    <div class="h4 fw-bold text-danger"><%= grandCancelled %></div>
                </div>
            </div>
        </div>

        <!-- Utilisation Report -->
        <div class="card border-0 shadow-sm mb-4">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-bar-chart text-info me-1"></i>
                Utilisation Report &mdash; <%= monthNames[selectedMonth] %> <%= selectedYear %>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Clinic</th><th>Service</th><th>Daily Quota</th>
                                <th>Total Bookings</th><th>Completed</th><th>No-Show</th>
                                <th>Cancelled</th><th>Utilisation</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (report == null || report.isEmpty()) { %>
                        <tr><td colspan="8" class="text-center text-muted py-3">No appointment data for this period.</td></tr>
                        <% } else {
                            // calculate days in the selected month
                            java.time.YearMonth ym = java.time.YearMonth.of(selectedYear, selectedMonth);
                            int daysInMonth = ym.lengthOfMonth();
                            for (Map<String, Object> row : report) {
                                int quota = (int) row.get("daily_quota");
                                int total = (int) row.get("total");
                                int capacity = quota * daysInMonth;
                                double utilPct = capacity > 0 ? Math.min(100.0 * total / capacity, 100.0) : 0;
                        %>
                        <tr>
                            <td class="fw-semibold"><%= row.get("clinic_name") %></td>
                            <td><%= row.get("service_name") %></td>
                            <td><%= quota %>/day</td>
                            <td><%= total %></td>
                            <td class="text-success fw-semibold"><%= row.get("completed") %></td>
                            <td class="text-warning fw-semibold"><%= row.get("no_show") %></td>
                            <td class="text-danger"><%= row.get("cancelled") %></td>
                            <td style="min-width:120px;">
                                <div class="d-flex align-items-center gap-2">
                                    <div class="utilisation-bar flex-grow-1">
                                        <div class="utilisation-fill" style="width:<%= String.format("%.1f", utilPct) %>%"></div>
                                    </div>
                                    <small class="text-muted"><%= String.format("%.0f", utilPct) %>%</small>
                                </div>
                                <div class="text-muted" style="font-size:11px;"><%= total %> / <%= capacity %></div>
                            </td>
                        </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Audit Log -->
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-journal-text text-secondary me-1"></i> Staff Action Audit Log
                <span class="text-muted small ms-2">(last 50 entries)</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr><th>Time</th><th>User</th><th>Action</th><th>Target</th><th>Description</th></tr>
                        </thead>
                        <tbody>
                        <% if (auditLogs == null || auditLogs.isEmpty()) { %>
                        <tr><td colspan="5" class="text-center text-muted py-3">No audit entries yet.</td></tr>
                        <% } else { for (Map<String, Object> log : auditLogs) { %>
                        <tr>
                            <td class="text-muted small"><%= DateTimeUtil.format((java.time.LocalDateTime) log.get("created_at")) %></td>
                            <td class="fw-semibold"><%= log.get("username") %></td>
                            <td><span class="badge bg-secondary"><%= log.get("action_type") %></span></td>
                            <td class="text-muted small"><%= log.get("target_type") %> #<%= log.get("target_id") %></td>
                            <td><%= log.get("description") %></td>
                        </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
