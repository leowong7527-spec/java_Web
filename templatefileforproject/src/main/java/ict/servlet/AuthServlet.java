package ict.servlet;

import ict.db.NotificationDB;
import ict.db.UserDB;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDB userDB;
    private NotificationDB notificationDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");

        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        userDB = new UserDB(dbUrl, dbUser, dbPassword);
        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = normalizePath(request.getPathInfo());

        if ("/logout".equals(path)) {
            AuthUtil.logout(request);
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        User currentUser = AuthUtil.currentUser(request);
        if (currentUser != null) {
            redirectByRole(request, response, currentUser);
            return;
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String view = "/register".equals(path) ? "/WEB-INF/jsp/auth/register.jsp" : "/WEB-INF/jsp/auth/login.jsp";
            RequestDispatcher rd = getServletContext().getRequestDispatcher(view);
            rd.forward(request, response);
            return;
        }

        if ("/register".equals(path)) {
            handleRegister(request, response);
            return;
        }

        handleLogin(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDB.authenticate(username, password);
        if (user == null) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            return;
        }

        AuthUtil.login(request, user);
        redirectByRole(request, response, user);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (isBlank(username) || isBlank(password) || isBlank(fullName)) {
            request.setAttribute("error", "Username, password and full name are required.");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }

        User user = userDB.registerPatient(username, password, fullName, phone, email);
        if (user == null) {
            request.setAttribute("error", "Username already exists.");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }

        notificationDB.create(user.getId(), "APPOINTMENT", "Welcome to CCHC. Your patient account is ready.");
        AuthUtil.login(request, user);
        response.sendRedirect(request.getContextPath() + "/patient/dashboard");
    }

    private void redirectByRole(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if ("STAFF".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/staff/template");
            return;
        }
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/patient/dashboard");
    }

    private String normalizePath(String pathInfo) {
        return pathInfo == null ? "/login" : pathInfo;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


