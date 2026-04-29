<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, ict.bean.Brand" %>
<html>
<head><title>Brands</title></head>
<body>
  <%
    ArrayList<Brand> brands = (ArrayList<Brand>)request.getAttribute("brands");
    for(Brand b : brands){
  %>
    <a href="getPhones?action=list&brand=<%= b.getName() %>"><%= b.getName() %></a><br/>
  <% } %>
</body>
</html>
