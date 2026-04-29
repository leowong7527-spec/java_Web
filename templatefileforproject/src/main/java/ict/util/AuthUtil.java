package ict.util;

import ict.bean.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class AuthUtil {
    private AuthUtil() {
    }

    public static void login(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole());
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object userObj = session.getAttribute("currentUser");
        if (userObj instanceof User) {
            return (User) userObj;
        }

        return null;
    }

    public static User requireRole(HttpServletRequest request, HttpServletResponse response, String role) throws IOException {
        User user = currentUser(request);
        if (user == null || !role.equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return null;
        }
        return user;
    }
}


