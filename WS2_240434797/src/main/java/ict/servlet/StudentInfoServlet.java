package ict.servlet;

import ict.util.HtmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling student information submissions
 *
 * @author a1
 */
@WebServlet(name = "StudentInfoServlet", urlPatterns = {"/StudentInfoServlet"})
public class StudentInfoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String studentName = request.getParameter("studentName");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String campus = request.getParameter("campus");
        String[] languages = request.getParameterValues("language");
        String instruction = request.getParameter("instruction");

        String secret = request.getParameter("secret");

        String missing = "MISSING";

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Student Information</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>You have entered:</h1>");

            // Personal Particular
            out.println("<p><strong>Name:</strong> " + ((!studentName.isEmpty()) ? studentName : missing) + "</p>");
            out.println("<p><strong>Password:</strong> " + ((!password.isEmpty()) ? password : missing) + "</p>");
            out.println("<p><strong>Gender:</strong> " + (gender != null ? gender : missing) + "</p>");
            out.println("<p><strong>Campus:</strong> " + (campus != null ? campus : missing) + "</p>");

            // Languages
            out.println("<p><strong>Languages:</strong> ");
            if (languages != null && languages.length > 0) {
                for (String lang : languages) {
                    out.println(lang + " ");
                }
            } else {
                out.print(missing);
            }
            out.println("</p>");

            // Instruction
            out.println("<p><strong>Instruction:</strong> " + (instruction != null && !instruction.isEmpty() ? instruction : missing) + "</p>");

            out.println("<p><strong>Secret:</strong>" + (secret != null && !secret.isEmpty() ? secret : missing) + "</p>");
            
            out.println("<p> " + "<strong>Request Parameter Names are:</strong> " + "</p>");
            
            java.util.Enumeration eParams = request.getParameterNames();
            while (eParams.hasMoreElements()) {
                String strParam = (String) eParams.nextElement();
                out.println(strParam);
            }

            

            out.println("<br/><a href=\"studentInfo.jsp\">BACK</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
