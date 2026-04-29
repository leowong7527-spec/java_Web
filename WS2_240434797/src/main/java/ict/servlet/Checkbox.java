package ict.servlet;

import ict.util.HtmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ide", urlPatterns = {"/checkbox"})
public class Checkbox extends HttpServlet {

    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String[] ideName = req.getParameterValues("ide");

        out.println("<html>");
        out.println("<head><title>IDE Selection Result</title></head>");
        out.println("<body>");
        out.println("<h1>IDE Selection Result</h1>");
        out.println("<p>Your choices are:</p>");
        
        if (ideName != null && ideName.length > 0) {
            out.println("<ul>");
            for (int i = 0; i < ideName.length; i++) {
                out.println("<li><strong>" + HtmlUtil.escapeHtml(ideName[i]) + "</strong></li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p>No IDE selected.</p>");
        }
        
        out.println("<br/><a href=\"checkbox.jsp\">Try again</a>");
        out.println("</body></html>");
    }
}
