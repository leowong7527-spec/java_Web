package ict.servlet;

import ict.db.AppointmentDB;
import ict.db.NotificationDB;
import ict.db.QueueDB;
import ict.db.ClinicServiceDB;
import ict.bean.Appointment;
import ict.bean.ClinicService;
import ict.bean.Notification;
import ict.bean.QueueEntry;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet("/patient/dashboard")
public class PatientDashboardServlet extends HttpServlet {
    private AppointmentDB appointmentDB;
    private QueueDB queueDB;
    private NotificationDB notificationDB;
    private ClinicServiceDB serviceDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        appointmentDB = new AppointmentDB(dbUrl, dbUser, dbPassword);
        queueDB = new QueueDB(dbUrl, dbUser, dbPassword);
        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
        serviceDB = new ClinicServiceDB(dbUrl, dbUser, dbPassword);
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

        List<Appointment> upcomingAppointments = appointmentDB.findUpcomingByPatient(user.getId());
        List<QueueEntry> todayQueue = queueDB.findTodayByPatient(user.getId());

        // Generate REMINDER notifications for appointments within the next 24 hours
        generateUpcomingReminders(user.getId(), upcomingAppointments);

        List<Notification> notifications = notificationDB.findByUser(user.getId());

        request.setAttribute("currentUser", user);
        request.setAttribute("upcomingAppointments", upcomingAppointments);
        request.setAttribute("todayQueue", todayQueue);
        request.setAttribute("notifications", notifications);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/dashboard.jsp");
        rd.forward(request, response);
    }

    private void generateUpcomingReminders(int patientId, List<Appointment> upcoming) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.plusHours(24);
        for (Appointment appt : upcoming) {
            if (appt.getSlotTime().isAfter(now) && !appt.getSlotTime().isAfter(cutoff)) {
                ClinicService svc = serviceDB.findById(appt.getServiceId());
                String serviceName = svc != null ? svc.getClinicName() + " - " + svc.getServiceName() : "service";
                String reminderMsg = "Reminder: Your appointment for " + serviceName
                        + " is on " + appt.getSlotTime().toLocalDate()
                        + " at " + appt.getSlotTime().toLocalTime() + ".";
                notificationDB.createIfAbsent(patientId, "REMINDER", reminderMsg);
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


