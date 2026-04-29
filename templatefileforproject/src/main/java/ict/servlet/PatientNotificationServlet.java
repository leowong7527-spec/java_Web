package ict.servlet;

import ict.db.NotificationDB;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/patient/notifications")
public class PatientNotificationServlet extends HttpServlet {
    private NotificationDB notificationDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) {
            return;
        }

        if ("true".equalsIgnoreCase(request.getParameter("markRead"))) {
            notificationDB.markAllRead(user.getId());
            request.setAttribute("message", "All notifications marked as read.");
            request.setAttribute("messageType", "success");
        }

        request.setAttribute("currentUser", user);
        request.setAttribute("notifications", notificationDB.findByUser(user.getId()));
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/notifications.jsp");
        rd.forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


