<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body> <%    // obtain the message parameter
        String message = "";
        if (request.getParameter("message") == null) {
            message = "Taglib is good";
        } else {
            message = request.getParameter("message");
        }
        %>
        <%@taglib uri="/WEB-INF/tlds/ict-taglib.tld" prefix="ict" %>
        <!--  message is dynamically evaluated-->
        <ict:icon message="<%=message%>" />

        <ict:icon message="<%=message%>" color="00FFFF" />
        <ict:icon message="<%=message%>" color="0000FF" />
        <ict:icon message="<%=message%>" color="00FF00" />

    </body>
</html>