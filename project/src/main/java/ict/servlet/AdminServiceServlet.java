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
import java.time.LocalTime;

@WebServlet("/admin/services")
public class AdminServiceServlet extends HttpServlet {
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
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/services.jsp");
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
                    String clinicName = trim(request.getParameter("clinicName"));
                    String serviceName = trim(request.getParameter("serviceName"));
                    int dailyQuota = Integer.parseInt(request.getParameter("dailyQuota"));
                    int slotCapacity = Integer.parseInt(request.getParameter("slotCapacity"));
                    LocalTime openingTime = LocalTime.parse(request.getParameter("openingTime"));
                    LocalTime closingTime = LocalTime.parse(request.getParameter("closingTime"));
                    if (blank(clinicName) || blank(serviceName) || dailyQuota < 1 || slotCapacity < 1) {
                        message = "All fields are required and quotas must be at least 1.";
                        messageType = "warning";
                    } else if (!openingTime.isBefore(closingTime)) {
                        message = "Opening time must be before closing time.";
                        messageType = "warning";
                    } else {
                        boolean ok = adminDB.createClinicService(clinicName, serviceName, dailyQuota, slotCapacity, openingTime, closingTime);
                        if (ok) {
                            adminDB.addAuditLog(admin.getId(), "CREATE_SERVICE", "CLINIC_SERVICE", 0,
                                    "Created service: " + clinicName + " - " + serviceName);
                            message = "Service created successfully.";
                            messageType = "success";
                        } else {
                            message = "Failed to create service.";
                            messageType = "error";
                        }
                    }
                    break;
                }
                case "edit": {
                    int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                    String clinicName = trim(request.getParameter("clinicName"));
                    String serviceName = trim(request.getParameter("serviceName"));
                    int dailyQuota = Integer.parseInt(request.getParameter("dailyQuota"));
                    int slotCapacity = Integer.parseInt(request.getParameter("slotCapacity"));
                    LocalTime openingTime = LocalTime.parse(request.getParameter("openingTime"));
                    LocalTime closingTime = LocalTime.parse(request.getParameter("closingTime"));
                    if (blank(clinicName) || blank(serviceName) || dailyQuota < 1 || slotCapacity < 1) {
                        message = "All fields are required and quotas must be at least 1.";
                        messageType = "warning";
                    } else if (!openingTime.isBefore(closingTime)) {
                        message = "Opening time must be before closing time.";
                        messageType = "warning";
                    } else {
                        boolean ok = adminDB.updateClinicService(serviceId, clinicName, serviceName, dailyQuota, slotCapacity, openingTime, closingTime);
                        if (ok) {
                            adminDB.addAuditLog(admin.getId(), "EDIT_SERVICE", "CLINIC_SERVICE", serviceId,
                                    "Updated service ID " + serviceId + ": " + clinicName + " - " + serviceName);
                            message = "Service updated successfully.";
                            messageType = "success";
                        } else {
                            message = "Service not found.";
                            messageType = "warning";
                        }
                    }
                    break;
                }
                case "delete": {
                    int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                    boolean ok = adminDB.deleteClinicService(serviceId);
                    if (ok) {
                        adminDB.addAuditLog(admin.getId(), "DELETE_SERVICE", "CLINIC_SERVICE", serviceId,
                                "Deleted service ID " + serviceId);
                        message = "Service deleted.";
                        messageType = "success";
                    } else {
                        message = "Delete failed. The service may have linked appointments.";
                        messageType = "warning";
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
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/services.jsp");
        rd.forward(request, response);
    }

    private void loadPage(HttpServletRequest request, User admin, String message, String messageType) {
        request.setAttribute("currentUser", admin);
        request.setAttribute("services", adminDB.listClinicServices());
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType == null ? "success" : messageType);
    }

    private String trim(String v) { return v == null ? "" : v.trim(); }
    private boolean blank(String v) { return v == null || v.trim().isEmpty(); }
}
