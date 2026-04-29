package ict.servlet;

import ict.db.*;
import ict.bean.*;
import ict.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/updateStatus")
public class StaffActionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "STAFF");
        if (user == null) return;

        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        String url = getServletContext().getInitParameter("dbUrl");
        String dbU = getServletContext().getInitParameter("dbUser");
        String dbP = getServletContext().getInitParameter("dbPassword");

        if ("queue".equals(type)) {
            new QueueDB(url, dbU, dbP).updateQueueStatus(id, status);
        } else {
            AppointmentDB appDb = new AppointmentDB(url, dbU, dbP);
            appDb.updateAppointmentStatus(id, status);
            Appointment app = appDb.findById(id);
            new NotificationDB(url, dbU, dbP).create(app.getPatientId(), "Status updated: " + status, "SYSTEM");
        }
        response.sendRedirect("template?msg=success");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "STAFF");
        if (user == null) return;

        String action = request.getParameter("action");
        String url = getServletContext().getInitParameter("dbUrl");
        String dbU = getServletContext().getInitParameter("dbUser");
        String dbP = getServletContext().getInitParameter("dbPassword");

        if ("cancelApp".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String reason = request.getParameter("reason");
            AppointmentDB appDb = new AppointmentDB(url, dbU, dbP);
            appDb.cancelByClinic(id, reason);
            Appointment app = appDb.findById(id);
            new NotificationDB(url, dbU, dbP).create(app.getPatientId(), "Cancelled by clinic: " + reason, "URGENT");
        } else if ("reportIssue".equals(action)) {
            String type = request.getParameter("issueType");
            String desc = request.getParameter("desc");
            new StaffDB(url, dbU, dbP).reportIssue(1, type, desc, user.getId());
        }
        response.sendRedirect("template?msg=success");
    }
}