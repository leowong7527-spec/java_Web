<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<ClinicService> services = (List<ClinicService>) request.getAttribute("services");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) messageType = "success";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinics &amp; Services - CCHC Admin</title>
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
        <h4 class="fw-bold mb-1">Clinics &amp; Services</h4>
        <p class="text-muted small mb-3">Configure clinic names, services, opening hours and capacity.</p>

        <% if (message != null) { %>
        <div class="alert alert-<%= "success".equals(messageType) ? "success" : ("warning".equals(messageType) ? "warning" : "danger") %> alert-dismissible fade show">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <div class="card border-0 shadow-sm mb-4">
            <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
                <span><i class="bi bi-plus-circle text-success me-1"></i> Add New Service</span>
                <button class="btn btn-sm btn-outline-success" type="button" data-bs-toggle="collapse" data-bs-target="#createForm">
                    <i class="bi bi-chevron-down"></i>
                </button>
            </div>
            <div class="collapse" id="createForm">
                <div class="card-body">
                    <form method="post" action="<%= request.getContextPath() %>/admin/services">
                        <input type="hidden" name="action" value="create">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Clinic Name *</label>
                                <input type="text" name="clinicName" class="form-control form-control-sm" placeholder="e.g. Chai Wan Clinic" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Service Name *</label>
                                <input type="text" name="serviceName" class="form-control form-control-sm" placeholder="e.g. General Consultation" required>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label small fw-semibold">Daily Quota *</label>
                                <input type="number" name="dailyQuota" class="form-control form-control-sm" min="1" value="20" required>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label small fw-semibold">Slot Capacity *</label>
                                <input type="number" name="slotCapacity" class="form-control form-control-sm" min="1" value="1" required>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label small fw-semibold">Opening Time *</label>
                                <input type="time" name="openingTime" class="form-control form-control-sm" value="09:00" required>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label small fw-semibold">Closing Time *</label>
                                <input type="time" name="closingTime" class="form-control form-control-sm" value="17:00" required>
                            </div>
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-success btn-sm w-100">
                                    <i class="bi bi-plus-lg me-1"></i>Add
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-hospital text-success me-1"></i> Configured Services
                <span class="badge bg-secondary ms-2"><%= services != null ? services.size() : 0 %></span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th><th>Clinic</th><th>Service</th><th>Daily Quota</th>
                                <th>Slot Capacity</th><th>Hours</th><th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (services == null || services.isEmpty()) { %>
                        <tr><td colspan="7" class="text-center text-muted py-3">No services configured.</td></tr>
                        <% } else { for (ClinicService svc : services) { %>
                        <tr>
                            <td class="text-muted small">#<%= svc.getId() %></td>
                            <td class="fw-semibold"><%= svc.getClinicName() %></td>
                            <td><%= svc.getServiceName() %></td>
                            <td><%= svc.getDailyQuota() %></td>
                            <td><%= svc.getSlotCapacity() %></td>
                            <td class="text-muted small"><%= svc.getOpeningTime() %> – <%= svc.getClosingTime() %></td>
                            <td>
                                <button class="btn btn-outline-secondary btn-sm me-1"
                                        data-bs-toggle="modal" data-bs-target="#editModal"
                                        data-serviceid="<%= svc.getId() %>"
                                        data-clinicname="<%= svc.getClinicName() %>"
                                        data-servicename="<%= svc.getServiceName() %>"
                                        data-dailyquota="<%= svc.getDailyQuota() %>"
                                        data-slotcapacity="<%= svc.getSlotCapacity() %>"
                                        data-openingtime="<%= svc.getOpeningTime() %>"
                                        data-closingtime="<%= svc.getClosingTime() %>">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <form method="post" action="<%= request.getContextPath() %>/admin/services" class="d-inline"
                                      onsubmit="return confirm('Delete service <%= svc.getServiceName() %>?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="serviceId" value="<%= svc.getId() %>">
                                    <button type="submit" class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Edit Modal -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Service</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/admin/services">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="serviceId" id="editServiceId">
                <div class="modal-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Clinic Name *</label>
                            <input type="text" name="clinicName" id="editClinicName" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Service Name *</label>
                            <input type="text" name="serviceName" id="editServiceName" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Daily Quota *</label>
                            <input type="number" name="dailyQuota" id="editDailyQuota" class="form-control" min="1" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Slot Capacity *</label>
                            <input type="number" name="slotCapacity" id="editSlotCapacity" class="form-control" min="1" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Opening Time *</label>
                            <input type="time" name="openingTime" id="editOpeningTime" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Closing Time *</label>
                            <input type="time" name="closingTime" id="editClosingTime" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.getElementById('editModal').addEventListener('show.bs.modal', function(event) {
    const btn = event.relatedTarget;
    document.getElementById('editServiceId').value = btn.dataset.serviceid;
    document.getElementById('editClinicName').value = btn.dataset.clinicname;
    document.getElementById('editServiceName').value = btn.dataset.servicename;
    document.getElementById('editDailyQuota').value = btn.dataset.dailyquota;
    document.getElementById('editSlotCapacity').value = btn.dataset.slotcapacity;
    document.getElementById('editOpeningTime').value = btn.dataset.openingtime;
    document.getElementById('editClosingTime').value = btn.dataset.closingtime;
});
</script>
</body>
</html>
