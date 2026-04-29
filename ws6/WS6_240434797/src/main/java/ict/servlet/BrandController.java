package ict.servlet;

import ict.bean.Brand;
import ict.servlet.AuthUtil;
import ict.db.BrandsDB;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "BrandController", urlPatterns = {"/brandController"})
public class BrandController extends HttpServlet {

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
        if (!AuthUtil.ensureAuthenticated(request, response, getServletContext())) {
            return;
        }
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            ArrayList<Brand> brands = db.getAllBrands();
            request.setAttribute("brands", brands);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/brands.jsp");
            rd.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }
}
