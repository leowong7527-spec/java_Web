<%@ page import="java.util.Map" %>
<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    Map<String, Integer> counts = (Map<String, Integer>) request.getAttribute("counts");
    int patients = counts != null ? counts.getOrDefault("patients", 0) : 0;
    int staff = counts != null ? counts.getOrDefault("staff", 0) : 0;
    int services = counts != null ? counts.getOrDefault("services", 0) : 0;
    int todayAppts = counts != null ? counts.getOrDefault("today_appointments", 0) : 0;
    int todayQueue = counts != null ? counts.getOrDefault("today_queue", 0) : 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - CCHC System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f0f4f8; }
        .sidebar { min-height: 100vh; background: linear-gradient(180deg, #0d3b66, #1565c0); }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); border-radius: 8px; margin: 2px 8px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: #fff; }
        .sidebar .nav-link i { width: 20px; }
        .stat-card { border: none; border-radius: 16px; box-shadow: 0 4px 16px rgba(0,0,0,0.07); }
        .stat-card .icon-box { width: 52px; height: 52px; border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 22px; }
    </style>
</head>
<body>
<div class="d-flex">
    <%@ include file="nav.jsp" %>
    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h4 class="mb-0 fw-bold">Admin Dashboard</h4>
                <small class="text-muted">Welcome back, <%= user.getFullName() %></small>
            </div>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-sm-6 col-xl-4">
                <div class="card stat-card p-3">
                    <div class="d-flex align-items-center gap-3">
                        <div class="icon-box bg-primary bg-opacity-10 text-primary"><i class="bi bi-people-fill"></i></div>
                        <div>
                            <div class="text-muted small">Registered Patients</div>
                            <div class="h4 mb-0 fw-bold"><%= patients %></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-xl-4">
                <div class="card stat-card p-3">
                    <div class="d-flex align-items-center gap-3">
                        <div class="icon-box bg-success bg-opacity-10 text-success"><i class="bi bi-person-badge-fill"></i></div>
                        <div>
                            <div class="text-muted small">Clinic Staff</div>
                            <div class="h4 mb-0 fw-bold"><%= staff %></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-xl-4">
                <div class="card stat-card p-3">
                    <div class="d-flex align-items-center gap-3">
                        <div class="icon-box bg-info bg-opacity-10 text-info"><i class="bi bi-hospital-fill"></i></div>
                        <div>
                            <div class="text-muted small">Clinic Services</div>
                            <div class="h4 mb-0 fw-bold"><%= services %></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-xl-4">
                <div class="card stat-card p-3">
                    <div class="d-flex align-items-center gap-3">
                        <div class="icon-box bg-warning bg-opacity-10 text-warning"><i class="bi bi-calendar-check-fill"></i></div>
                        <div>
                            <div class="text-muted small">Today's Appointments</div>
                            <div class="h4 mb-0 fw-bold"><%= todayAppts %></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-xl-4">
                <div class="card stat-card p-3">
                    <div class="d-flex align-items-center gap-3">
                        <div class="icon-box bg-danger bg-opacity-10 text-danger"><i class="bi bi-person-lines-fill"></i></div>
                        <div>
                            <div class="text-muted small">Today's Waiting Queue</div>
                            <div class="h4 mb-0 fw-bold"><%= todayQueue %></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-3">
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/admin/users" class="card stat-card p-4 text-decoration-none text-dark d-block">
                    <i class="bi bi-people fs-2 text-primary"></i>
                    <div class="mt-2 fw-semibold">User Management</div>
                    <div class="text-muted small">Create, edit and delete user accounts</div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/admin/services" class="card stat-card p-4 text-decoration-none text-dark d-block">
                    <i class="bi bi-gear-fill fs-2 text-success"></i>
                    <div class="mt-2 fw-semibold">Clinic &amp; Services</div>
                    <div class="text-muted small">Configure clinics, services and hours</div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/admin/reports" class="card stat-card p-4 text-decoration-none text-dark d-block">
                    <i class="bi bi-bar-chart-fill fs-2 text-info"></i>
                    <div class="mt-2 fw-semibold">Reports &amp; Analytics</div>
                    <div class="text-muted small">Utilisation rates and no-show summaries</div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/admin/policy" class="card stat-card p-4 text-decoration-none text-dark d-block">
                    <i class="bi bi-shield-check fs-2 text-warning"></i>
                    <div class="mt-2 fw-semibold">Policy Settings</div>
                    <div class="text-muted small">Max bookings, cancellation cutoff, queue rules</div>
                </a>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
