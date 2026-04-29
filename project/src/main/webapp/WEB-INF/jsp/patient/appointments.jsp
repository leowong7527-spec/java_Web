<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="ict.bean.User" %>
<%@ page import="ict.bean.Appointment" %>
<%@ page import="ict.bean.ClinicService" %>
<%@ page import="ict.util.DateTimeUtil" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%
    User user = (User) request.getAttribute("currentUser");
    List<ClinicService> services = (List<ClinicService>) request.getAttribute("services");
    List<ClinicService> allServices = (List<ClinicService>) request.getAttribute("allServices");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    List<String> clinicOptions = (List<String>) request.getAttribute("clinicOptions");
    List<LocalDateTime> availableSlots = (List<LocalDateTime>) request.getAttribute("availableSlots");
    String selectedClinic = (String) request.getAttribute("selectedClinic");
    String selectedServiceId = (String) request.getAttribute("selectedServiceId");
    String selectedDate = (String) request.getAttribute("selectedDate");
    String earliestBookingDate = (String) request.getAttribute("earliestBookingDate");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("messageType");
    if (messageType == null) {
        messageType = "success";
    }
    if (allServices == null) {
        allServices = services;
    }
    Map<Integer, ClinicService> serviceMap = new HashMap<>();
    for (ClinicService service : allServices) {
        serviceMap.put(service.getId(), service);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Appointments - CCHC Patient Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/app.css"/>
</head>
<body>
<div class="topbar">
    <a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a>
    <a class="active" href="<%= request.getContextPath() %>/patient/appointments">Appointments</a>
    <a href="<%= request.getContextPath() %>/patient/queue">Queue</a>
    <a href="<%= request.getContextPath() %>/patient/notifications">Notifications</a>
    <a href="<%= request.getContextPath() %>/patient/profile">Profile</a>
    <a href="<%= request.getContextPath() %>/auth/logout">Logout</a>
</div>
<div class="container">
    <div class="hero">
        <div class="toolbar">
            <div>
                <div class="section-title">Appointment Management</div>
                <h1>Book, reschedule, and cancel with live availability</h1>
                <p class="muted">Choose a clinic, pick a service, select a date, then book only from available time
                    slots.</p>
            </div>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="muted">Total records</span>
            <span class="value"><%= appointments.size() %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Selected clinic</span>
            <span class="value"
                  style="font-size: 20px;"><%= selectedClinic == null ? "All clinics" : selectedClinic %></span>
        </div>
        <div class="summary-card">
            <span class="muted">Selected date</span>
            <span class="value" style="font-size: 20px;"><%= selectedDate %></span>
        </div>
    </div>

    <div class="filter-bar panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Search availability</div>
                <h3>Filter by clinic, service, and date</h3>
            </div>
        </div>
        <p class="muted" style="margin-top:-4px; margin-bottom:14px;">Choose a clinic first, then pick a matching service and date. Available slots will load immediately after the selection is complete.</p>
        <form method="get" action="<%= request.getContextPath() %>/patient/appointments" class="grid-3" id="availability-form">
            <div>
                <label>Clinic</label>
                <select name="clinic" id="clinic-select" required>
                    <option value="">All clinics</option>
                    <% for (String clinic : clinicOptions) { %>
                    <option value="<%= clinic %>" <%= clinic.equals(selectedClinic) ? "selected" : "" %>><%= clinic %>
                    </option>
                    <% } %>
                </select>
            </div>
            <div>
                <label>Service</label>
                <select name="serviceId" id="service-select" required>
                    <option value="">Choose a service</option>
                    <% for (ClinicService service : allServices) { %>
                    <option value="<%= service.getId() %>" data-clinic="<%= service.getClinicName() %>" <%= String.valueOf(service.getId()).equals(selectedServiceId) ? "selected" : "" %>><%= service.getClinicName() %> - <%= service.getServiceName() %> (<%= service.getOpeningTime() %>-<%= service.getClosingTime() %>)
                    </option>
                    <% } %>
                </select>
            </div>
            <div>
                <label>Date</label>
                <input type="date" name="date" id="date-input" value="<%= selectedDate %>" min="<%= earliestBookingDate %>" required/>
            </div>
            <div style="grid-column: 1 / -1; display:flex; justify-content:flex-end; gap:10px; flex-wrap:wrap;">
                <button type="submit" style="width:auto; min-width: 180px;">Show available slots</button>
            </div>
        </form>
    </div>

    <div class="panel" id="booking-panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Available slots</div>
                <h3>Click a slot to book immediately</h3>
            </div>
        </div>
        <% if (message != null) { %>
        <div class="alert alert-<%= messageType %>"><%= message %>
        </div>
        <% } %>
        <% if (selectedClinic == null || selectedClinic.isEmpty() || selectedServiceId == null || selectedServiceId.isEmpty()) { %>
        <p class="muted">Select a clinic, matching service, and date above to load available times.</p>
        <% } else if (availableSlots == null || availableSlots.isEmpty()) { %>
        <p class="muted">No available slots for the selected service and date.</p>
        <% } else { %>
        <div class="slot-grid">
            <% for (LocalDateTime slot : availableSlots) { %>
            <div class="slot-card">
                <div style="font-weight:700; color:#0f4c81;"><%= DateTimeUtil.format(slot) %>
                </div>
                <div class="muted">Available</div>
                <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                    <input type="hidden" name="action" value="book"/>
                    <input type="hidden" name="clinic" value="<%= selectedClinic == null ? "" : selectedClinic %>"/>
                    <input type="hidden" name="serviceId" value="<%= selectedServiceId %>"/>
                    <input type="hidden" name="date" value="<%= selectedDate %>"/>
                    <input type="hidden" name="slotTime" value="<%= slot %>"/>
                    <button type="submit">Book slot</button>
                </form>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">Manage bookings</div>
                <h3>Reschedule or cancel</h3>
            </div>
        </div>
        <div class="grid-2">
            <div class="card" style="margin-bottom:0;">
                <h3>Reschedule Appointment</h3>
                <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                    <input type="hidden" name="action" value="reschedule"/>
                    <label>Appointment ID</label>
                    <input type="number" name="appointmentId" required/>
                    <label>New Date and Time</label>
                    <input type="datetime-local" name="newSlot" required/>
                    <button type="submit">Reschedule</button>
                </form>
            </div>

            <div class="card" style="margin-bottom:0;">
                <h3>Quick Notes</h3>
                <p class="muted">Double booking is blocked. Bookings now follow selected slot availability, so the page
                    feels closer to a real hospital portal.</p>
                <p class="muted">If you need to cancel, use the history table below.</p>
            </div>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head">
            <div>
                <div class="section-title">History</div>
                <h3>Your appointment records</h3>
            </div>
        </div>
        <table>
            <tr>
                <th>ID</th>
                <th>Service</th>
                <th>Slot</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <% if (appointments.isEmpty()) { %>
            <tr>
                <td colspan="5" class="muted">No appointment records.</td>
            </tr>
            <% } %>
            <% for (Appointment appointment : appointments) {
                ClinicService service = serviceMap.get(appointment.getServiceId());
            %>
            <tr>
                <td>#<%= appointment.getId() %>
                </td>
                <td><%= service == null ? "N/A" : service.getClinicName() + " - " + service.getServiceName() %>
                </td>
                <td><%= DateTimeUtil.format(appointment.getSlotTime()) %>
                </td>
                <td><ui:statusBadge value="<%= appointment.getStatus() %>"/></td>
                <td>
                    <% if (!"CANCELLED".equals(appointment.getStatus())) { %>
                    <form method="post" action="<%= request.getContextPath() %>/patient/appointments">
                        <input type="hidden" name="action" value="cancel"/>
                        <input type="hidden" name="appointmentId" value="<%= appointment.getId() %>"/>
                        <button type="submit">Cancel</button>
                    </form>
                    <% } else { %>
                    <span class="muted">-</span>
                    <% } %>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
<script>
(function () {
    const clinicSelect = document.getElementById('clinic-select');
    const serviceSelect = document.getElementById('service-select');
    const dateInput = document.getElementById('date-input');
    const form = document.getElementById('availability-form');

    function syncServices() {
        const clinic = clinicSelect.value;
        let selectedVisible = false;

        Array.from(serviceSelect.options).forEach(function (option) {
            if (!option.value) {
                option.hidden = false;
                option.disabled = false;
                return;
            }

            const matchesClinic = !clinic || option.dataset.clinic === clinic;
            option.hidden = !matchesClinic;
            option.disabled = !matchesClinic;

            if (!matchesClinic && option.selected) {
                option.selected = false;
            }

            if (option.selected && matchesClinic) {
                selectedVisible = true;
            }
        });

        serviceSelect.disabled = !clinic;
        if (!clinic || !selectedVisible) {
            serviceSelect.value = '';
        }
    }

    function maybeSubmit() {
        if (clinicSelect.value && serviceSelect.value && dateInput.value) {
            form.submit();
        }
    }

    clinicSelect.addEventListener('change', function () {
        syncServices();
        serviceSelect.value = '';
    });
    serviceSelect.addEventListener('change', maybeSubmit);
    dateInput.addEventListener('change', maybeSubmit);

    syncServices();
})();
</script>
</body>
</html>

