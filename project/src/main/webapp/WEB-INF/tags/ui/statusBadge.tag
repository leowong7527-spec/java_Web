<%@ tag description="Render a unified status badge" pageEncoding="UTF-8" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="tone" required="false" %>
<%
    String text = value == null ? "" : value.toString().trim();
    String normalized = text.toUpperCase();
    String badgeTone = tone == null ? "" : tone.toString().trim().toLowerCase();

    if (badgeTone.isEmpty()) {
        if (normalized.equals("BOOKED") || normalized.equals("CALLING") || normalized.equals("PENDING")) {
            badgeTone = "warning";
        } else if (normalized.equals("ARRIVED") || normalized.equals("COMPLETED") || normalized.equals("SERVED") || normalized.equals("READ")) {
            badgeTone = "success";
        } else if (normalized.equals("CANCELLED") || normalized.equals("NO-SHOW") || normalized.equals("NOSHOW") || normalized.equals("EXPIRED") || normalized.equals("UNREAD")) {
            badgeTone = "danger";
        } else if (normalized.equals("WAITING")) {
            badgeTone = "info";
        } else {
            badgeTone = "neutral";
        }
    }
%>
<span class="status-badge badge-<%= badgeTone %>"><%= text.isEmpty() ? "-" : text %></span>