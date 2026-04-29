package ict.servlet;

import ict.db.CustomerDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class HandleEdit extends HttpServlet {
    private CustomerDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");

        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        db = new CustomerDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        if (isBlank(action)) {
            response.sendRedirect("handleCustomer?action=list");
            return;
        }

        String id = trimToEmpty(request.getParameter("id"));
        String name = trimToEmpty(request.getParameter("name"));
        String tel = trimToEmpty(request.getParameter("tel"));
        int age = parseIntOrDefault(request.getParameter("age"), -1);

        if (isBlank(id) || isBlank(name) || age < 0) {
            response.sendRedirect("handleCustomer?action=list");
            return;
        }

        try {
            if ("add".equals(action)) {
                db.addRecord(id, name, tel, age);
            } else if ("edit".equals(action)) {
                db.editRecord(id, name, tel, age);
            }

            response.sendRedirect("handleCustomer?action=list");
        } catch (SQLException e) {
            throw new ServletException("Failed to update customer", e);
        }
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
