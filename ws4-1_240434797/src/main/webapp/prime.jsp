<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/primes.tld" prefix="ict" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Prime Result</title>
</head>
<body>
<%
    int min = 2;
    int max = 50;
    String tagType = "simple";

    try {
        if (request.getParameter("min") != null) {
            min = Integer.parseInt(request.getParameter("min"));
        }
        if (request.getParameter("max") != null) {
            max = Integer.parseInt(request.getParameter("max"));
        }
        if (request.getParameter("tagType") != null) {
            tagType = request.getParameter("tagType");
        }
    } catch (NumberFormatException ignored) {
    }
%>

<h2>Prime Numbers (<%=min%> to <%=max%>)</h2>
<ict:prime min="<%=min%>" max="<%=max%>" tagType="<%=tagType%>" />
<p><a href="primeInput.jsp">Back</a></p>
</body>
</html>
