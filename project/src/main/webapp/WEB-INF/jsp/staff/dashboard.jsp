<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard - CCHC System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root { --cchc-primary: #0d6efd; --cchc-secondary: #6c757d; --cchc-bg: #f8f9fa; }
        body { background-color: var(--cchc-bg); font-family: 'Inter', system-ui, sans-serif; }
        .navbar { box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .card { border: none; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); margin-bottom: 24px; transition: transform 0.2s; }
        .card-header { background-color: white; border-bottom: 1px solid #edf2f7; font-weight: 600; padding: 1.25rem; border-radius: 12px 12px 0 0 !important; }
        .table thead th { background-color: #fcfcfc; text-transform: uppercase; font-size: 0.75rem; letter-spacing: 0.05em; color: #718096; border-top: none; }
        .status-badge { padding: 0.4em 0.8em; border-radius: 6px; font-size: 0.85rem; font-weight: 600; }
        .status-booked { background-color: #ebf8ff; color: #2b6cb0; }
        .status-arrived { background-color: #f0fff4; color: #2f855a; }
        .status-calling { background-color: #fffaf0; color: #c05621; animation: pulse 2s infinite; }
        .status-waiting { background-color: #edf2f7; color: #4a5568; }
        @keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.6; } 100% { opacity: 1; } }
        .btn-action { padding: 0.4rem 0.8rem; font-size: 0.8rem; font-weight: 500; border-radius: 6px; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top mb-4">
    <div class="container">
        <a class="navbar-brand" href="#"><i class="bi bi-hospital"></i> CCHC Staff Portal</a>
        <div class="d-flex align-items-center">
            <span class="text-white me-3"><i class="bi bi-person-circle"></i> Welcome, ${currentUser.username}</span>
            <a href="../auth/logout" class="btn btn-outline-light btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container">
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Action completed successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row text-center mb-2">
        <div class="col-md-4">
            <div class="card p-3">
                <div class="text-muted small">Today's Appointments</div>
                <div class="h3 mb-0 text-primary">${appointments.size()}</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3">
                <div class="text-muted small">Pending Queue</div>
                <div class="h3 mb-0 text-warning">${queues.size()}</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3 text-success">
                <div class="text-muted small">Clinic Status</div>
                <div class="h3 mb-0">Operational</div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <span><i class="bi bi-calendar-check text-primary"></i> Appointment List</span>
            <span class="badge bg-light text-dark">Today</span>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Patient ID</th>
                            <th>Status</th>
                            <th>Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="app" items="${appointments}">
                            <tr>
                                <td class="fw-bold">${app.slotTime}</td>
                                <td><i class="bi bi-person"></i> ${app.patientId}</td>
                                <td>
                                    <span class="status-badge status-${app.status.toLowerCase()}">${app.status}</span>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <a href="updateStatus?id=${app.id}&status=ARRIVED" class="btn btn-outline-success btn-action">Check-in</a>
                                        <a href="updateStatus?id=${app.id}&status=NOSHOW" class="btn btn-outline-secondary btn-action">No Show</a>
                                        <a href="updateStatus?id=${app.id}&status=COMPLETED" class="btn btn-primary btn-action">Finish</a>
                                    </div>
                                    <form action="updateStatus" method="POST" class="d-inline-flex ms-2">
                                        <input type="hidden" name="action" value="cancelApp">
                                        <input type="hidden" name="id" value="${app.id}">
                                        <div class="input-group input-group-sm" style="width: 180px;">
                                            <input type="text" name="reason" class="form-control" placeholder="Reason" required>
                                            <button class="btn btn-danger" type="submit">Cancel</button>
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center bg-white">
            <span><i class="bi bi-people text-warning"></i> Walk-in Queue</span>
            <button class="btn btn-sm btn-light" onclick="location.reload()"><i class="bi bi-arrow-clockwise"></i> Refresh</button>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Queue No.</th>
                            <th>Patient</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="q" items="${queues}">
                            <tr>
                                <td class="h5 mb-0">#${q.queueNumber}</td>
                                <td>Patient ID: ${q.patientId}</td>
                                <td>
                                    <span class="status-badge status-${q.status.toLowerCase()}">${q.status}</span>
                                </td>
                                <td>
                                    <a href="updateStatus?type=queue&id=${q.id}&status=CALLING" class="btn btn-warning btn-action">Call Next</a>
                                    <a href="updateStatus?type=queue&id=${q.id}&status=SERVED" class="btn btn-success btn-action">Served</a>
                                    <a href="updateStatus?type=queue&id=${q.id}&status=SKIPPED" class="btn btn-link text-muted btn-sm text-decoration-none">Skip</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="card shadow-sm border-warning">
        <div class="card-header bg-warning-subtle">
            <i class="bi bi-exclamation-triangle"></i> Report Operational Issue
        </div>
        <div class="card-body">
            <form action="updateStatus" method="POST" class="row g-3">
                <input type="hidden" name="action" value="reportIssue">
                <div class="col-md-4">
                    <label class="form-label small fw-bold">Issue Category</label>
                    <select name="issueType" class="form-select" required>
                        <option value="Doctor Unavailable">Doctor Unavailable</option>
                        <option value="Service Suspended">Service Suspended</option>
                        <option value="System Technical Error">System Technical Error</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label small fw-bold">Description</label>
                    <input type="text" name="desc" class="form-control" placeholder="e.g. Dr. Wong is away until 2PM">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-warning w-100 fw-bold">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

