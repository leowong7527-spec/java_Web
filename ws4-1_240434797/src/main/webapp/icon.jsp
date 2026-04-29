<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/ict-taglib.tld" prefix="ict" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Icon Example</title>
</head>
<body>
<%
    String message = request.getParameter("message");
    if (message == null || message.trim().isEmpty()) {
        message = "Taglib is good";
    }
%>
    <ict:icon message="<%=message%>" />
    <ict:icon message="<%=message%>" color="00FFFF" />
    <ict:icon message="<%=message%>" color="0000FF" />
    <ict:icon message="<%=message%>" color="00FF00" />
</body>
</html>
