/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

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
@WebServlet(name= "Calculate",urlPatterns = {"/calculate"})
public class Calculate extends HttpServlet {

       @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String value1 = req.getParameter("value1");
        String value2 = req.getParameter("value2");
        String operation = req.getParameter("operation");
        
        if(!(value1.isEmpty()||value2.isEmpty()||operation.isEmpty())){
            out.println("<html>");
            out.println("<head><title>Simple Calculator</title></head>");
            out.println("<body>");
            out.println("The");
            out.println("</body></html>");
        }else{
            out.println("<html>");
            out.println("<head><title>Error</title></head>");
            out.println("<body>");
            out.println("Some info is missing");
            out.println("<a href=\"calculatorInput.jsp\">calculatorInput.jsp</a>");
            out.println("</body></html>");
        }
        
    }
}
