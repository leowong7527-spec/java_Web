<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/Add-taglib.tld" prefix="add" %>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
  </head>
  <body>
    <h1><add:add num1="10" num2="20" /></h1>
    <h1><add:add num1="10" num2="0" /></h1>
    <h1><add:add num1="0" num2="0" /></h1>
  </body>
</html>
