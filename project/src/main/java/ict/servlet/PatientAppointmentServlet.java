package ict.servlet;

import ict.db.AppointmentDB;
import ict.db.ClinicServiceDB;
import ict.db.NotificationDB;
import ict.bean.Appointment;
import ict.bean.ClinicService;
import ict.bean.User;
import ict.util.AuthUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/patient/appointments")
public class PatientAppointmentServlet extends HttpServlet {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int SLOT_MINUTES = 30;
    private static final int BOOKING_CUTOFF_HOURS = 24;

    private AppointmentDB appointmentDB;
    private ClinicServiceDB serviceDB;
    private NotificationDB notificationDB;

    @Override
    public void init() throws ServletException {
        String dbUrl = getServletContext().getInitParameter("dbUrl");
        String dbUser = getServletContext().getInitParameter("dbUser");
        String dbPassword = getServletContext().getInitParameter("dbPassword");
        if (isBlank(dbUrl) || isBlank(dbUser) || dbPassword == null) {
            throw new ServletException("Database parameters are missing in web.xml");
        }

        appointmentDB = new AppointmentDB(dbUrl, dbUser, dbPassword);
        serviceDB = new ClinicServiceDB(dbUrl, dbUser, dbPassword);
        notificationDB = new NotificationDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = AuthUtil.requireRole(request, response, "PATIENT");
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");
        if (isBlank(action)) {
            action = "view";
        }
        String message;
        String messageType;

        try {
            switch (action) {
                case "book":
                    int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                    String selectedClinic = normalize(request.getParameter("clinic"));
                    LocalDateTime slotTime = LocalDateTime.parse(request.getParameter("slotTime"));
                    ClinicService bookedService = serviceDB.findById(serviceId);
                    if (!isServiceSelectionValid(selectedClinic, bookedService)) {
                        message = "Please choose a clinic and matching service before booking.";
                        messageType = "warning";
                        break;
                    }
                    if (bookedService == null) {
                        message = "Selected service is not available.";
                        messageType = "warning";
                        break;
                    }
                    if (!isBookableSlot(bookedService, slotTime)) {
                        message = "Booking is only allowed at least 24 hours in advance.";
                        messageType = "warning";
                        break;
                    }
                    if (appointmentDB.hasActiveFutureAppointment(user.getId())) {
                        message = "You already have an active future appointment.";
                        messageType = "warning";
                        break;
                    }
                    if (appointmentDB.hasSlotConflict(serviceId, slotTime, bookedService.getSlotCapacity())) {
                        message = "Selected slot is full.";
                        messageType = "warning";
                        break;
                    }
                    Appointment appointment = appointmentDB.createAppointment(user.getId(), serviceId, slotTime, bookedService.getSlotCapacity());
                    if (appointment == null) {
                        message = "Booking failed: capacity limit reached or duplicate booking detected.";
                        messageType = "error";
                    } else {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Appointment booked: " + bookedService.getServiceName());
                        request.setAttribute("currentUser", user);
                        request.setAttribute("appointment", appointment);
                        request.setAttribute("service", bookedService);
                        request.setAttribute("bookingCutoffHours", BOOKING_CUTOFF_HOURS);
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/appointment-confirmation.jsp");
                        rd.forward(request, response);
                        return;
                    }
                    break;
                case "reschedule":
                    int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
                    LocalDateTime newSlot = LocalDateTime.parse(request.getParameter("newSlot"));
                    Appointment existingAppointment = appointmentDB.findById(appointmentId);
                    if (existingAppointment == null || existingAppointment.getPatientId() != user.getId()) {
                        message = "Appointment not found.";
                        messageType = "warning";
                        break;
                    }
                    ClinicService appointmentService = serviceDB.findById(existingAppointment.getServiceId());
                    if (appointmentService == null) {
                        message = "Appointment service is not available.";
                        messageType = "warning";
                        break;
                    }
                    if (!isBookableSlot(appointmentService, newSlot)) {
                        message = "Reschedule is only allowed at least 24 hours in advance.";
                        messageType = "warning";
                        break;
                    }
                    if (appointmentDB.hasSlotConflict(appointmentService.getId(), newSlot, appointmentService.getSlotCapacity())) {
                        message = "Selected reschedule slot is full.";
                        messageType = "warning";
                        break;
                    }
                    boolean rescheduled = appointmentDB.rescheduleAppointment(appointmentId, user.getId(), newSlot);
                    if (rescheduled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment was rescheduled.");
                        message = "Appointment rescheduled.";
                        messageType = "success";
                    } else {
                        message = "Reschedule failed.";
                        messageType = "warning";
                    }
                    break;
                case "cancel":
                    int cancelId = Integer.parseInt(request.getParameter("appointmentId"));
                    boolean cancelled = appointmentDB.cancelAppointment(cancelId, user.getId());
                    if (cancelled) {
                        notificationDB.create(user.getId(), "APPOINTMENT", "Your appointment has been cancelled.");
                        message = "Appointment cancelled.";
                        messageType = "success";
                    } else {
                        message = "Cancellation failed.";
                        messageType = "warning";
                    }
                    break;
                case "list":
                case "view":
                    message = null;
                    messageType = null;
                    break;
                default:
                    message = "Unsupported action.";
                    messageType = "error";
                    break;
            }
        } catch (Exception ex) {
            message = "Request failed. Please check input format.";
            messageType = "error";
        }

        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType == null ? "success" : messageType);
        }
        loadPageData(request, user);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/jsp/patient/appointments.jsp");
        rd.forward(request, response);
    }

    private void loadPageData(HttpServletRequest request, User user) {
        String selectedClinic = normalize(request.getParameter("clinic"));
        String selectedServiceId = normalize(request.getParameter("serviceId"));
        String selectedDate = normalize(request.getParameter("date"));

        List<ClinicService> allServices = serviceDB.findAll();
        List<String> clinicOptions = allServices.stream()
                .map(ClinicService::getClinicName)
                .distinct()
                .collect(Collectors.toList());

        List<ClinicService> services = allServices;
        if (!isBlank(selectedClinic)) {
            final String clinicFilter = selectedClinic;
            services = allServices.stream()
                .filter(service -> clinicFilter.equals(service.getClinicName()))
                    .collect(Collectors.toList());
        }

        LocalDate selectedLocalDate = null;
        if (!isBlank(selectedDate)) {
            selectedLocalDate = LocalDate.parse(selectedDate);
        }

        ClinicService selectedService = null;
        if (!isBlank(selectedServiceId)) {
            int serviceId = Integer.parseInt(selectedServiceId);
            selectedService = serviceDB.findById(serviceId);
            if (selectedService != null && isBlank(selectedClinic)) {
                selectedClinic = selectedService.getClinicName();
            } else if (selectedService != null && !isBlank(selectedClinic) && !selectedClinic.equals(selectedService.getClinicName())) {
                selectedService = null;
                selectedServiceId = null;
                request.setAttribute("message", "Please choose a service that belongs to the selected clinic.");
                request.setAttribute("messageType", "warning");
            }
        }

        List<LocalDateTime> availableSlots = buildAvailableSlots(selectedService, selectedLocalDate);
        List<Appointment> appointments = appointmentDB.findByPatient(user.getId());
        LocalDate earliestBookableDate = LocalDate.now().plusDays(1);

        request.setAttribute("clinicOptions", clinicOptions);
        request.setAttribute("appointments", appointments);
        request.setAttribute("availableSlots", availableSlots);
        request.setAttribute("selectedClinic", selectedClinic);
        request.setAttribute("selectedServiceId", selectedServiceId);
        request.setAttribute("selectedDate", selectedLocalDate == null ? earliestBookableDate.format(DATE_FORMAT) : selectedLocalDate.format(DATE_FORMAT));
        request.setAttribute("selectedService", selectedService);
        request.setAttribute("currentUser", user);
        request.setAttribute("services", services);
        request.setAttribute("allServices", allServices);
        request.setAttribute("earliestBookingDate", earliestBookableDate.format(DATE_FORMAT));
    }

    private List<LocalDateTime> buildAvailableSlots(ClinicService selectedService, LocalDate selectedDate) {
        List<LocalDateTime> slots = new ArrayList<>();
        if (selectedService == null || selectedDate == null) {
            return slots;
        }

        LocalDateTime minimumBookableDateTime = LocalDateTime.now().plusHours(BOOKING_CUTOFF_HOURS);
        LocalDate minimumBookableDate = minimumBookableDateTime.toLocalDate();
        if (selectedDate.isBefore(minimumBookableDate)) {
            return slots;
        }

        Map<LocalDateTime, Integer> bookedSlotCounts = appointmentDB.findBookedSlotCountsByServiceAndDate(selectedService.getId(), selectedDate);
        LocalDateTime cursor = selectedDate.atTime(selectedService.getOpeningTime());
        LocalDateTime end = selectedDate.atTime(selectedService.getClosingTime());

        if (selectedDate.equals(minimumBookableDate)) {
            cursor = alignToNextSlot(minimumBookableDateTime);
            if (cursor.isBefore(selectedDate.atTime(selectedService.getOpeningTime()))) {
                cursor = selectedDate.atTime(selectedService.getOpeningTime());
            }
        }

        while (cursor.isBefore(end)) {
            int bookedCount = bookedSlotCounts.getOrDefault(cursor, 0);
            if (bookedCount < selectedService.getSlotCapacity()) {
                slots.add(cursor);
            }
            cursor = cursor.plusMinutes(SLOT_MINUTES);
        }
        return slots;
    }

    private LocalDateTime alignToNextSlot(LocalDateTime time) {
        LocalDateTime normalized = time.withSecond(0).withNano(0);
        int minute = normalized.getMinute();
        int remainder = minute % SLOT_MINUTES;
        if (remainder == 0 && time.getSecond() == 0 && time.getNano() == 0) {
            return normalized;
        }
        int minutesToAdd = remainder == 0 ? SLOT_MINUTES : SLOT_MINUTES - remainder;
        return normalized.plusMinutes(minutesToAdd);
    }

    private boolean isBookableSlot(ClinicService service, LocalDateTime slotTime) {
        if (service == null || slotTime == null) {
            return false;
        }

        LocalDateTime minimumBookableDateTime = LocalDateTime.now().plusHours(BOOKING_CUTOFF_HOURS);
        LocalDateTime start = slotTime.toLocalDate().atTime(service.getOpeningTime());
        LocalDateTime end = slotTime.toLocalDate().atTime(service.getClosingTime());
        if (slotTime.isBefore(minimumBookableDateTime)) {
            return false;
        }
        if (slotTime.isBefore(start)) {
            return false;
        }
        return !slotTime.plusMinutes(SLOT_MINUTES).isAfter(end);
    }

    private boolean isServiceSelectionValid(String selectedClinic, ClinicService selectedService) {
        if (selectedService == null) {
            return false;
        }
        return isBlank(selectedClinic) || selectedClinic.equals(selectedService.getClinicName());
    }

    private String normalize(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


