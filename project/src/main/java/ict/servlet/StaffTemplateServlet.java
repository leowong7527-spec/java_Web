package ict.servlet;

import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/staff/template")
public class StaffTemplateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = AuthUtil.requireRole(request, response, "STAFF");
        if (user == null) {
            return;
        }

        request.setAttribute("currentUser", user);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/staff/dashboard.jsp");
        rd.forward(request, response);
    }
}


