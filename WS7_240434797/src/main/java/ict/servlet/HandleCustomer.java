package ict.servlet;

import ict.bean.CustomerBean;
import ict.db.CustomerDB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandleCustomer extends HttpServlet {
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
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (isBlank(action)) {
            action = "list";
        }

        try {
            switch (action) {
                case "delete":
                    deleteCustomer(request, response);
                    break;
                case "search":
                    searchCustomers(request, response);
                    break;
                case "getEditCustomer":
                    getEditCustomer(request, response);
                    break;
                case "list":
                default:
                    listCustomers(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database operation failed", e);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        ArrayList<CustomerBean> customers = db.queryCust();
        request.setAttribute("customers", customers);
        RequestDispatcher rd = request.getRequestDispatcher("/listCustomers.jsp");
        rd.forward(request, response);
    }

    private void searchCustomers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        if (name == null) {
            name = "";
        }

        ArrayList<CustomerBean> customers = db.queryCustByName(name.trim());
        request.setAttribute("customers", customers);
        request.setAttribute("searchName", name);
        RequestDispatcher rd = request.getRequestDispatcher("/listCustomers.jsp");
        rd.forward(request, response);
    }

    private void getEditCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String id = request.getParameter("id");
        if (isBlank(id)) {
            response.sendRedirect("handleCustomer?action=list");
            return;
        }

        CustomerBean customer = db.queryCustByID(id);
        if (customer == null) {
            response.sendRedirect("handleCustomer?action=list");
            return;
        }

        request.setAttribute("c", customer);
        RequestDispatcher rd = request.getRequestDispatcher("/editCustomer.jsp");
        rd.forward(request, response);
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String id = request.getParameter("id");
        if (!isBlank(id)) {
            db.delRecord(id);
        }
        response.sendRedirect("handleCustomer?action=list");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
