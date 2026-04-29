package ict.servlet;

import ict.bean.Phone;
import ict.db.BrandsDB;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PhoneController", urlPatterns = {"/getPhones"})
public class PhoneController extends HttpServlet {

    private BrandsDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        db = new BrandsDB(dbUrl, dbUser, dbPassword);
        db.initialize();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            String brand = request.getParameter("brand");
            if (brand == null || brand.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing brand parameter");
                return;
            }
            ArrayList<Phone> phones = db.getPhonesByBrand(brand);
            request.setAttribute("phoneList", phones);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/phoneList.jsp");
            rd.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }
}
