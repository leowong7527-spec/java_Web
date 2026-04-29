package ict.servlet;

import ict.bean.User;
import ict.db.AdminDB;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private AdminDB adminDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        adminDB = new AdminDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User admin = AuthUtil.requireRole(request, response, "ADMIN");
        if (admin == null) return;
        loadPage(request, admin, null, null);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User admin = AuthUtil.requireRole(request, response, "ADMIN");
        if (admin == null) return;

        String action = request.getParameter("action");
        String message;
        String messageType;

        try {
            switch (action == null ? "" : action) {
                case "create": {
                    String username = trim(request.getParameter("username"));
                    String password = trim(request.getParameter("password"));
                    String fullName = trim(request.getParameter("fullName"));
                    String phone = trim(request.getParameter("phone"));
                    String email = trim(request.getParameter("email"));
                    String role = trim(request.getParameter("role"));
                    if (blank(username) || blank(password) || blank(fullName) || blank(role)) {
                        message = "Username, password, full name and role are required.";
                        messageType = "warning";
                    } else if (!isValidRole(role)) {
                        message = "Invalid role selected.";
                        messageType = "warning";
                    } else {
                        boolean ok = adminDB.createUser(username, password, fullName, phone, email, role);
                        if (ok) {
                            adminDB.addAuditLog(admin.getId(), "CREATE_USER", "USER", 0,
                                    "Created user: " + username + " (" + role + ")");
                            message = "User '" + username + "' created successfully.";
                            messageType = "success";
                        } else {
                            message = "Username already exists.";
                            messageType = "warning";
                        }
                    }
                    break;
                }
                case "edit": {
                    int userId = Integer.parseInt(request.getParameter("userId"));
                    String fullName = trim(request.getParameter("fullName"));
                    String phone = trim(request.getParameter("phone"));
                    String email = trim(request.getParameter("email"));
                    String role = trim(request.getParameter("role"));
                    String newPassword = trim(request.getParameter("newPassword"));
                    if (blank(fullName) || blank(role)) {
                        message = "Full name and role are required.";
                        messageType = "warning";
                    } else if (!isValidRole(role)) {
                        message = "Invalid role selected.";
                        messageType = "warning";
                    } else {
                        boolean ok = adminDB.updateUser(userId, fullName, phone, email, role, newPassword);
                        if (ok) {
                            adminDB.addAuditLog(admin.getId(), "EDIT_USER", "USER", userId,
                                    "Updated user ID " + userId + " role=" + role);
                            message = "User updated successfully.";
                            messageType = "success";
                        } else {
                            message = "User update failed.";
                            messageType = "warning";
                        }
                    }
                    break;
                }
                case "delete": {
                    int userId = Integer.parseInt(request.getParameter("userId"));
                    if (userId == admin.getId()) {
                        message = "You cannot delete your own account.";
                        messageType = "warning";
                    } else {
                        boolean ok = adminDB.deleteUser(userId);
                        if (ok) {
                            adminDB.addAuditLog(admin.getId(), "DELETE_USER", "USER", userId,
                                    "Deleted user ID " + userId);
                            message = "User deleted.";
                            messageType = "success";
                        } else {
                            message = "Delete failed. User may have linked records.";
                            messageType = "warning";
                        }
                    }
                    break;
                }
                default:
                    message = "Unknown action.";
                    messageType = "error";
            }
        } catch (Exception ex) {
            message = "Request failed: " + ex.getMessage();
            messageType = "error";
        }

        loadPage(request, admin, message, messageType);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp");
        rd.forward(request, response);
    }

    private void loadPage(HttpServletRequest request, User admin, String message, String messageType) {
        request.setAttribute("currentUser", admin);
        request.setAttribute("users", adminDB.listAllUsers());
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType == null ? "success" : messageType);
    }

    private boolean isValidRole(String role) {
        return "PATIENT".equals(role) || "STAFF".equals(role) || "ADMIN".equals(role);
    }

    private String trim(String v) { return v == null ? "" : v.trim(); }
    private boolean blank(String v) { return v == null || v.trim().isEmpty(); }
}
