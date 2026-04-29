<%@ page import="ict.bean.Appointment" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Appointment appointment = (Appointment) request.getAttribute("appointment");
    ClinicService service = (ClinicService) request.getAttribute("service");
    Integer bookingCutoffHours = (Integer) request.getAttribute("bookingCutoffHours");
    if (bookingCutoffHours == null) {
        bookingCutoffHours = 24;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Confirmed - CCHC Patient Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="section-title">Booking Confirmation</div>
        <h1>Your appointment is confirmed</h1>
        <p class="muted">A confirmation notification has been created. You can review it now or continue managing your bookings.</p>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Appointment Details</div>
                <h3>Booking reference and time</h3>
            </div>
            <span class="badge badge-success">Confirmed</span>
        </div>
        <div class="grid-2">
            <div class="card" style="margin-bottom:0;">
                <div class="muted">Booking ID</div>
                <h3>#<%= appointment.getId() %></h3>
                <div class="muted">Status</div>
                <p><%= appointment.getStatus() %></p>
                <div class="muted">Created at</div>
                <p><%= DateTimeUtil.format(appointment.getCreatedAt()) %></p>
            </div>
            <div class="card" style="margin-bottom:0;">
                <div class="muted">Clinic</div>
                <h3><%= service.getClinicName() %></h3>
                <div class="muted">Service</div>
                <p><%= service.getServiceName() %></p>
                <div class="muted">Appointment time</div>
                <p><%= DateTimeUtil.format(appointment.getSlotTime()) %></p>
            </div>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Policy</div>
                <h3>Booking cutoff</h3>
            </div>
        </div>
        <p class="muted">Appointments must be booked at least <%= bookingCutoffHours %> hours in advance and cannot be created on the same day.</p>
        <div class="panel-actions">
            <a class="action-link" href="<%= request.getContextPath() %>/patient/appointments">Back to appointments</a>
            <a class="action-link" href="<%= request.getContextPath() %>/patient/dashboard">Go to dashboard</a>
        </div>
    </div>
</div>
</body>
</html>
