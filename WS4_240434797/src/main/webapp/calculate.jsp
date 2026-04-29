<%-- 
    Document   : calculate
    Created on : Feb 11, 2026, 11:50:43 AM
    Author     : a1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <!-- Create the bean object -->
        <jsp:useBean id="cBean" class="ict.bean.CalculateBean" scope="request" />

        <jsp:useBean id="cHelper" class="ict.util.CalculateHelper" scope="request" />

        <%

            int value1 = Integer.parseInt(request.getParameter("value1"));
            int value2 = Integer.parseInt(request.getParameter("value2"));
            String operator = request.getParameter("operator");

//
//            cBean.setValue1(value1);
//            cBean.setValue2(value2);
//            cBean.setOperator(operator);

        %>

        <!-- Update bean properties -->
        <jsp:setProperty name="cBean" property="value1" value="<%=value1%>"/>
        <jsp:setProperty name="cBean" property="value2" value="<%=value2%>"/>
        <jsp:setProperty name="cBean" property="operator" value="<%=operator%>"/>

        <%

            int result = 0;
            try {
                result = cBean.calculate();
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        %>
        <!-- Output bean values -->
        <p>
            <jsp:getProperty name="cBean" property="value1"/> 
            <jsp:getProperty name="cBean" property="operator"/> 
            <jsp:getProperty name="cBean" property="value2"/>
        </p>

        </br>
        <p>The result is <%= result%></p>
    </body>
</html>
