<%-- 
    Document   : editCustomer
    Created on : Feb 11, 2026, 11:47:29 AM
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
        <form action="handleCustomer2.jsp" method="post">
        <label for="id">ID:</label>
        <input type="text" id="custId" name="custId" value="" /><br/><br/>

        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="" /><br/><br/>

        <label for="tel">Tel:</label>
        <input type="text" id="tel" name="tel" value="" /><br/><br/>

        <label for="age">Age:</label>
        <input type="text" id="age" name="age" value="" /><br/><br/>

        <input type="submit" value="Submit" />
    </form>
    </body>
</html>
