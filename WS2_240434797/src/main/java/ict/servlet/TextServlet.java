package ict.servlet;

import ict.util.HtmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "TextServlet", urlPatterns = {"/hello"})
public class TextServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String name = req.getParameter("name");
        
        if(name == null || name.isEmpty()){
            out.println("<html>");
            out.println("<head><title>Hello World</title></head>");
            out.println("<body>");
            out.println("<h1>Hello World</h1>");
            out.println("<p>Please enter your name.</p>");
            out.println("<br/><a href=\"text.jsp\">Try again</a>");
            out.println("</body></html>");
        }else{
            out.println("<html>");
            out.println("<head><title>Hello, " + HtmlUtil.escapeHtml(name) + "</title></head>");
            out.println("<body>");
            out.println("<h1>Hello, " + HtmlUtil.escapeHtml(name) + "</h1>");
            out.println("<br/><a href=\"text.jsp\">Enter another name</a>");
            out.println("</body></html>");
        }
    }
}
