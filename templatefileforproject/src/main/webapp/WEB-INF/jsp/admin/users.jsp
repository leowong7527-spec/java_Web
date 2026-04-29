<%@ page import="java.util.List" %>
<%@ page import="ict.bean.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) request.getAttribute("currentUser");
    @SuppressWarnings("unchecked")
    List<User> users = (List<User>) request.getAttribute("users");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) messageType = "success";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - CCHC Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { background-color: #f0f4f8; }
        .sidebar { min-height: 100vh; background: linear-gradient(180deg, #0d3b66, #1565c0); }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); border-radius: 8px; margin: 2px 8px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: #fff; }
        .sidebar .nav-link i { width: 20px; }
        .role-badge-PATIENT { background: #dbeafe; color: #1e40af; }
        .role-badge-STAFF { background: #dcfce7; color: #166534; }
        .role-badge-ADMIN { background: #fef3c7; color: #92400e; }
    </style>
</head>
<body>
<div class="d-flex">
    <%@ include file="nav.jsp" %>
    <div class="flex-grow-1 p-4">
        <h4 class="fw-bold mb-1">User Management</h4>
        <p class="text-muted small mb-3">Create, edit, and delete patient, staff and admin accounts.</p>

        <% if (message != null) { %>
        <div class="alert alert-<%= "success".equals(messageType) ? "success" : ("warning".equals(messageType) ? "warning" : "danger") %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <div class="card border-0 shadow-sm mb-4">
            <div class="card-header bg-white fw-semibold d-flex justify-content-between align-items-center">
                <span><i class="bi bi-person-plus text-primary me-1"></i> Create New User</span>
                <button class="btn btn-sm btn-outline-primary" type="button" data-bs-toggle="collapse" data-bs-target="#createForm">
                    <i class="bi bi-chevron-down"></i>
                </button>
            </div>
            <div class="collapse" id="createForm">
                <div class="card-body">
                    <form method="post" action="<%= request.getContextPath() %>/admin/users">
                        <input type="hidden" name="action" value="create">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Username *</label>
                                <input type="text" name="username" class="form-control form-control-sm" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Password *</label>
                                <input type="password" name="password" class="form-control form-control-sm" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Full Name *</label>
                                <input type="text" name="fullName" class="form-control form-control-sm" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">Role *</label>
                                <select name="role" class="form-select form-select-sm" required>
                                    <option value="PATIENT">PATIENT</option>
                                    <option value="STAFF">STAFF</option>
                                    <option value="ADMIN">ADMIN</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">Phone</label>
                                <input type="text" name="phone" class="form-control form-control-sm">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">Email</label>
                                <input type="email" name="email" class="form-control form-control-sm">
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary btn-sm w-100">
                                    <i class="bi bi-plus-lg me-1"></i>Create User
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white fw-semibold">
                <i class="bi bi-people text-primary me-1"></i> All Users
                <span class="badge bg-secondary ms-2"><%= users != null ? users.size() : 0 %></span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th><th>Username</th><th>Full Name</th><th>Role</th>
                                <th>Phone</th><th>Email</th><th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (users == null || users.isEmpty()) { %>
                        <tr><td colspan="7" class="text-center text-muted py-3">No users found.</td></tr>
                        <% } else { for (User u : users) { %>
                        <tr>
                            <td class="text-muted small">#<%= u.getId() %></td>
                            <td class="fw-semibold"><%= u.getUsername() %></td>
                            <td><%= u.getFullName() %></td>
                            <td><span class="badge role-badge-<%= u.getRole() %>"><%= u.getRole() %></span></td>
                            <td class="text-muted small"><%= u.getPhone() == null ? "-" : u.getPhone() %></td>
                            <td class="text-muted small"><%= u.getEmail() == null ? "-" : u.getEmail() %></td>
                            <td>
                                <button class="btn btn-outline-secondary btn-sm me-1"
                                        data-bs-toggle="modal" data-bs-target="#editModal"
                                        data-userid="<%= u.getId() %>"
                                        data-fullname="<%= u.getFullName() %>"
                                        data-phone="<%= u.getPhone() == null ? "" : u.getPhone() %>"
                                        data-email="<%= u.getEmail() == null ? "" : u.getEmail() %>"
                                        data-role="<%= u.getRole() %>">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <% if (u.getId() != user.getId()) { %>
                                <form method="post" action="<%= request.getContextPath() %>/admin/users" class="d-inline"
                                      onsubmit="return confirm('Delete user <%= u.getUsername() %>?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="userId" value="<%= u.getId() %>">
                                    <button type="submit" class="btn btn-outline-danger btn-sm"><i class="bi bi-trash"></i></button>
                                </form>
                                <% } %>
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
                <h5 class="modal-title">Edit User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/admin/users">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="userId" id="editUserId">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Full Name *</label>
                        <input type="text" name="fullName" id="editFullName" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Role *</label>
                        <select name="role" id="editRole" class="form-select" required>
                            <option value="PATIENT">PATIENT</option>
                            <option value="STAFF">STAFF</option>
                            <option value="ADMIN">ADMIN</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Phone</label>
                        <input type="text" name="phone" id="editPhone" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Email</label>
                        <input type="email" name="email" id="editEmail" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">New Password <span class="text-muted">(leave blank to keep current)</span></label>
                        <input type="password" name="newPassword" class="form-control">
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
    document.getElementById('editUserId').value = btn.dataset.userid;
    document.getElementById('editFullName').value = btn.dataset.fullname;
    document.getElementById('editPhone').value = btn.dataset.phone;
    document.getElementById('editEmail').value = btn.dataset.email;
    document.getElementById('editRole').value = btn.dataset.role;
});
</script>
</body>
</html>
