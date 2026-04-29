<%@ page import="ict.bean.CustomerBean" %>
<%
    CustomerBean c = (CustomerBean) request.getAttribute("c");
    String action = (c == null) ? "add" : "edit";
%>

<h1><%= (action.equals("add") ? "Add Customer" : "Edit Customer") %></h1>

<form method="post" action="handleEdit">
    <input type="hidden" name="action" value="<%= action %>" />

    ID: <input type="text" name="id" value="<%= (c != null ? c.getCustId() : "") %>" <%= (c != null ? "readonly" : "") %> /><br>
    Name: <input type="text" name="name" value="<%= (c != null ? c.getName() : "") %>" /><br>
    Tel: <input type="text" name="tel" value="<%= (c != null ? c.getTel() : "") %>" /><br>
    Age: <input type="text" name="age" value="<%= (c != null ? c.getAge() : "") %>" /><br>

    <input type="submit" value="Submit" />
    <a href="handleCustomer?action=list">Cancel</a>
</form>
