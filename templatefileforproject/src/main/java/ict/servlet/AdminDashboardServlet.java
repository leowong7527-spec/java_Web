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
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
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
        User user = AuthUtil.requireRole(request, response, "ADMIN");
        if (user == null) return;

        Map<String, Integer> counts = adminDB.getDashboardCounts();
        request.setAttribute("currentUser", user);
        request.setAttribute("counts", counts);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp");
        rd.forward(request, response);
    }
}
