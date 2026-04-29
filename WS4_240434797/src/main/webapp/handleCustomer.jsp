<%-- 
    Document   : handleCustomer
    Created on : Feb 25, 2026, 10:45:54 AM
    Author     : a1
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="ict.bean.CustomerBean" %>

<%! 
    // Declare an ArrayList to temporarily store customers
    ArrayList<CustomerBean> customers = new ArrayList<CustomerBean>();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Handle Customer</title>
</head>
<body>
    <!-- Create a CustomerBean with id 'c' -->
    <jsp:useBean id="c" class="ict.bean.CustomerBean" scope="page" />

    <!-- Set properties from input form -->
    <jsp:setProperty name="c" property="custId" />
    <jsp:setProperty name="c" property="name" />
    <jsp:setProperty name="c" property="tel" />
    <jsp:setProperty name="c" property="age" />

    <p>The customer information are as follows:</p>
    <!-- Display current inputted values -->
    <p>
        ID: <jsp:getProperty name="c" property="custId" /><br/>
        Name: <jsp:getProperty name="c" property="name" /><br/>
        Tel: <jsp:getProperty name="c" property="tel" /><br/>
        Age: <jsp:getProperty name="c" property="age" /><br/>
    </p>

    <%
        // Add the current customer to the list
        customers.add(c);
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
            for (CustomerBean cust : customers) {
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

