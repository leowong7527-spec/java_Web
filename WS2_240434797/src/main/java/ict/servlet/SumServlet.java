/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ict.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author a1
 */
@WebServlet(name = "SumServlet", urlPatterns = {"/sum"})
public class SumServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        String n1 = req.getParameter("n1");
        String n2 = req.getParameter("n2");
        
        String msg1 = "Value 1 is an invalid input and assumed to be zero";
        String msg2 = "Value 2 is an invalid input and assumed to be zero";
        
        Integer value1 = 0;
        Integer value2 = 0;

        out.println("<html>");
        out.println("<head><title>Calculator Result</title></head>");
        out.println("<body>");
        out.println("<h1>Calculator Result</h1>");

        try {
            value1 = Integer.parseInt(n1);
        } catch(Exception e) {
            out.println("<p style=\"color: orange;\">" + msg1 + "</p>");
        }

        try {
            value2 = Integer.parseInt(n2);
        } catch(Exception e) {
            out.println("<p style=\"color: orange;\">" + msg2 + "</p>");
        }
        
        out.println("<p>The sum of <strong>" + value1 + "</strong> and <strong>" + value2 + "</strong> is <strong>" + (value1+value2) + "</strong></p>");
        out.println("<br/><a href=\"calculateServlet.jsp\">Calculate again</a>");
        out.println("</body></html>");
    }
        
}
