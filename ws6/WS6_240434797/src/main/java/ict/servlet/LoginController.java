package ict.servlet;

import ict.bean.UserInfo;
import ict.db.UserDB;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="LoginController", urlPatterns={"/main"})
public class LoginController extends HttpServlet {
    
    private UserDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        db = new UserDB(dbUrl, dbUser, dbPassword);
        db.createUserInfoTable();
        db.addUserInfo("1", "abc", "123");
        db.addUserInfo("2", "xyz", "123");
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

        if (!isAuthenticated(request) && !"authenticate".equals(action)) {
            doLogin(request, response);
            return;
        }

        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        } else {
            switch (action) {
                case "authenticate":
                    doAuthenticate(request, response);
                    break;
                case "logout":
                    doLogout(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                    break;
            }
        }
    }

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String targetURL;
        boolean isValid = db.isValidUser(username, password);

        if (isValid) {
            HttpSession session = request.getSession(true);
            UserInfo bean = new UserInfo();
            bean.setUsername(username);
            bean.setPassword(password);
            session.setAttribute("userInfo", bean);
            targetURL = "welcome.jsp";
        } else {
            targetURL = "loginError.jsp";
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/" + targetURL);
        rd.forward(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("userInfo") != null;
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("userInfo");
            session.invalidate();
        }
        doLogin(request, response);
    }
}
