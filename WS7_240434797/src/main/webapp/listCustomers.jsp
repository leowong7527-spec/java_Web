<%@ page import="java.util.ArrayList, ict.bean.CustomerBean" %>
<%
ArrayList<CustomerBean> customers = (ArrayList<CustomerBean>) request.getAttribute("customers");
if (customers == null) {
    customers = new ArrayList<>();
}
out.println("<h1>Customers</h1>");
out.println("<p><a href='editCustomer.jsp'>Add Customer</a> | <a href='searchCustomer.jsp'>Search Customer</a></p>");
out.println("<table border='1' cellpadding='6' cellspacing='0'>");
out.println("<tr><th>CustId</th><th>Name</th><th>Tel</th><th>Age</th><th>Delete</th><th>Edit</th></tr>");
for (CustomerBean c : customers) {
    out.println("<tr>");
    out.println("<td>" + c.getCustId() + "</td>");
    out.println("<td>" + c.getName() + "</td>");
    out.println("<td>" + c.getTel() + "</td>");
    out.println("<td>" + c.getAge() + "</td>");
    out.println("<td><a href='handleCustomer?action=delete&id=" + c.getCustId() + "'>delete</a></td>");
    out.println("<td><a href='handleCustomer?action=getEditCustomer&id=" + c.getCustId() + "'>edit</a></td>");
    out.println("</tr>");
}
out.println("</table>");
%>
