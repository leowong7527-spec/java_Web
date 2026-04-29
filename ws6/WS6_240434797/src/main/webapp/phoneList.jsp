<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.bean.Phone"%>
<!DOCTYPE html>
<html>
<head>
    <title>Phone List</title>
</head>
<body>
    <h2>Available Phones</h2>
    <%
        ArrayList<Phone> phoneList = (ArrayList<Phone>) request.getAttribute("phoneList");
        if (phoneList != null && !phoneList.isEmpty()) {
    %>
        <table border="1">
            <tr>
                <th>Image</th>
                <th>Name</th>
                <th>Price</th>
            </tr>
            <%
                for (Phone p : phoneList) {
            %>
            <tr>
                <td><img src="<%= p.getImg() %>" alt="<%= p.getName() %>" /></td>
                <td><%= p.getName() %></td>
                <td><%= p.getPrice() %></td>
            </tr>
            <% } %>
        </table>
    <%
        } else {
    %>
        <p>No phones found for this brand.</p>
    <%
        }
    %>
    <br/>
    <a href="brandController?action=list">Show Brands</a>
</body>
</html>
