
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    private int add(int v1, int v2) {
        int sum = v1 + v2;
        return sum;
    }

    private int subtract(int v1, int v2) {
        int sum = v1 - v2;
        return sum;
    }

    private int multiple(int v1, int v2) {
        int sum = v1 * v2;
        return sum;
    }

    private int toInt(String v) {
        int n = Integer.valueOf(v);
        return n;
    }

    int sum;
%>

<%@ page errorPage="HandleError.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Calculator</title>
    </head>
    <body> 
        
        <%
            int value1 = toInt(request.getParameter("value1"));
        %>

        <% int value2 = toInt(request.getParameter("value2"));
            String operation = request.getParameter("operation");

        %>

        <h1>Simple Calculator</h1>
        <p>The <%= value1%> <%= operation%> <%= value2%>.</p>



        <%
                if (operation == null) {
                    throw new Exception();
                }

                if (operation.equals("+")) {
                    sum = add(value1, value2);
                } else if (operation.equals("-")) {
                 sum = subtract(value1,value2);
                } else{
                sum = multiple(value1,value2);
                }
           

        %>


        <p>The result is <%= sum%></p>

    </body>
</html>

