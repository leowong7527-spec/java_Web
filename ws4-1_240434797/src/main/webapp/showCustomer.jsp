<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.bean.CustomerBean"%>
<%@ taglib uri="/WEB-INF/tlds/customers.tld" prefix="ict" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show Customer</title>
</head>
<body>
<%
    ArrayList<CustomerBean> customers = new ArrayList<>();
    customers.add(new CustomerBean("1234", "Peter", "12345678", 20));
    customers.add(new CustomerBean("45678", "Nancy", "87654321", 17));
%>

<h1>Simple</h1>
<ict:showCustomer customers="<%=customers%>" tagType="simple" />
<h1>List</h1>
<ict:showCustomer customers="<%=customers%>" tagType="list" />
</body>
</html>
