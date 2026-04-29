package ict.servlet;

import ict.util.HtmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "Radio", urlPatterns = {"/radio"})
public class Radio extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String size = req.getParameter("employee");
        
        out.println("<html>");
        out.println("<head><title>Company Size Result</title></head>");
        out.println("<body>");
        out.println("<h1>Company Size Result</h1>");
        
        if(size == null || size.isEmpty()){
            out.println("<p>No company size selected.</p>");
        } else {
            out.println("<p>Your company size: <strong>" + HtmlUtil.escapeHtml(size) + "</strong></p>");
        }
        
        out.println("<br/><a href=\"radio.jsp\">Try again</a>");
        out.println("</body></html>");
    }
}
