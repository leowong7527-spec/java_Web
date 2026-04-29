package ict.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthUtil {

    private AuthUtil() {
    }

    public static boolean ensureAuthenticated(HttpServletRequest request, HttpServletResponse response,
                                              ServletContext context) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userInfo") != null) {
            return true;
        }
        RequestDispatcher rd = context.getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
        return false;
    }
}
