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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {
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

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        if (yearParam != null && !yearParam.isEmpty()) {
            try { year = Integer.parseInt(yearParam); } catch (NumberFormatException ignored) {}
        }
        if (monthParam != null && !monthParam.isEmpty()) {
            try { month = Integer.parseInt(monthParam); } catch (NumberFormatException ignored) {}
        }

        List<Map<String, Object>> report = adminDB.getMonthlyReport(year, month);
        List<Map<String, Object>> auditLogs = adminDB.listAuditLogs(50);

        request.setAttribute("currentUser", admin);
        request.setAttribute("report", report);
        request.setAttribute("auditLogs", auditLogs);
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedMonth", month);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/reports.jsp");
        rd.forward(request, response);
    }
}
