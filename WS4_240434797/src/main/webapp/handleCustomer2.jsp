<%-- 
    Document   : handleCustomer2
    Created on : Feb 25, 2026, 10:55:00 AM
    Author     : a1
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="ict.bean.CustomerBean" %>
<%@ page import="ict.bean.CustomersBean" %>

<!DOCTYPE html>
<html>
<head>
    <title>Handle Customer 2</title>
</head>
<body>
    <!-- Create CustomersBean to store list of CustomerBean -->
    <jsp:useBean id="customersBean" class="ict.bean.CustomersBean" scope="session" />

    <!-- Create a CustomerBean for current input -->
    <jsp:useBean id="c" class="ict.bean.CustomerBean" scope="page" />

    <!-- Set properties from input form -->
    <jsp:setProperty name="c" property="custId" />
    <jsp:setProperty name="c" property="name" />
    <jsp:setProperty name="c" property="tel" />
    <jsp:setProperty name="c" property="age" />

    <p>The customer information are as follows:</p>
    <p>
        ID: <jsp:getProperty name="c" property="custId" /><br/>
        Name: <jsp:getProperty name="c" property="name" /><br/>
        Tel: <jsp:getProperty name="c" property="tel" /><br/>
        Age: <jsp:getProperty name="c" property="age" /><br/>
    </p>

    <%
        // Add the current customer to CustomersBean
        customersBean.addCustomer(c);
    %>

    <!-- Display all customers in a table -->
    <h3>Customer List</h3>
    <table border="1" cellpadding="5">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Tel</th>
            <th>Age</th>
        </tr>
        <%
            for (CustomerBean cust : customersBean.getCustomers()) {
        %>
        <tr>
            <td><%= cust.getCustId() %></td>
            <td><%= cust.getName() %></td>
            <td><%= cust.getTel() %></td>
            <td><%= cust.getAge() %></td>
        </tr>
        <%
            }
        %>
    </table>

    <!-- Link to input again -->
    <p><a href="editCustomer.jsp">Input Again</a></p>
</body>
</html>
