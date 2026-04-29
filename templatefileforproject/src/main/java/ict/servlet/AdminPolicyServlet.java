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
import java.util.List;
import java.util.Map;

@WebServlet("/admin/policy")
public class AdminPolicyServlet extends HttpServlet {
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
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/policy.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User admin = AuthUtil.requireRole(request, response, "ADMIN");
        if (admin == null) return;

        String message;
        String messageType;

        try {
            List<Map<String, String>> settings = adminDB.listPolicySettings();
            int updated = 0;
            for (Map<String, String> setting : settings) {
                String key = setting.get("key");
                String newValue = request.getParameter(key);
                if (newValue != null && !newValue.trim().isEmpty()) {
                    boolean ok = adminDB.updatePolicySetting(key, newValue.trim());
                    if (ok) {
                        adminDB.addAuditLog(admin.getId(), "UPDATE_POLICY", "POLICY", 0,
                                "Updated policy: " + key + " = " + newValue.trim());
                        updated++;
                    }
                }
            }
            message = updated + " policy setting(s) saved.";
            messageType = "success";
        } catch (Exception ex) {
            message = "Failed to save settings: " + ex.getMessage();
            messageType = "error";
        }

        loadPage(request, admin, message, messageType);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/policy.jsp");
        rd.forward(request, response);
    }

    private void loadPage(HttpServletRequest request, User admin, String message, String messageType) {
        request.setAttribute("currentUser", admin);
        request.setAttribute("settings", adminDB.listPolicySettings());
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType == null ? "success" : messageType);
    }
}
