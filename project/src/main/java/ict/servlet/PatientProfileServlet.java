package ict.servlet;

import ict.db.NotificationDB;
import ict.db.UserDB;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/patient/profile")
public class PatientProfileServlet extends HttpServlet {
    private UserDB userDB;
    private NotificationDB notificationDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        userDB = new UserDB(dbUrl, dbUser, dbPassword);
        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");
        if (isBlank(action)) {
            action = "view";
        }

        String message;
        String messageType;

        switch (action) {
            case "updateProfile":
                userDB.updateProfile(
                        user.getId(),
                        request.getParameter("fullName"),
                        request.getParameter("phone"),
                        request.getParameter("email")
                );
                notificationDB.create(user.getId(), "PROFILE", "Profile updated successfully.");
                message = "Profile updated.";
                messageType = "success";
                break;
            case "changePassword":
                boolean changed = userDB.updatePassword(
                        user.getId(),
                        request.getParameter("oldPassword"),
                        request.getParameter("newPassword")
                );
                if (changed) {
                    notificationDB.create(user.getId(), "PROFILE", "Password changed successfully.");
                    message = "Password updated.";
                    messageType = "success";
                } else {
                    message = "Password update failed. Check current password.";
                    messageType = "warning";
                }
                break;
            case "view":
                message = null;
                messageType = null;
                break;
            default:
                message = "Unsupported profile action.";
                messageType = "error";
                break;
        }

        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType == null ? "success" : messageType);
        }
        request.setAttribute("currentUser", userDB.findById(user.getId()));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/profile.jsp");
        rd.forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


