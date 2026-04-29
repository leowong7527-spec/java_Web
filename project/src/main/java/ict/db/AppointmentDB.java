package ict.db;

import ict.bean.Appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AppointmentDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public AppointmentDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public synchronized Appointment createAppointment(int patientId, int serviceId, LocalDateTime slotTime, int slotCapacity) {
        if (hasActiveFutureAppointment(patientId)) {
            return null;
        }
        if (hasDoubleBooking(patientId, slotTime, -1)) {
            return null;
        }
        if (hasSlotConflict(serviceId, slotTime, slotCapacity)) {
            return null;
        }

        String sql = "INSERT INTO appointments (patient_id, service_id, slot_time, status, created_at, updated_at) VALUES (?, ?, ?, 'BOOKED', ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patientId);
            ps.setInt(2, serviceId);
            ps.setTimestamp(3, Timestamp.valueOf(slotTime));
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return findById(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create appointment", ex);
        }
        return null;
    }

    public synchronized boolean rescheduleAppointment(int appointmentId, int patientId, LocalDateTime newSlot) {
        Appointment appointment = findById(appointmentId);
        if (appointment == null || appointment.getPatientId() != patientId || "CANCELLED".equals(appointment.getStatus())) {
            return false;
        }
        if (hasDoubleBooking(patientId, newSlot, appointmentId)) {
            return false;
        }

        String sql = "UPDATE appointments SET slot_time = ?, status = 'RESCHEDULED', updated_at = ? WHERE id = ? AND patient_id = ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(newSlot));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, appointmentId);
            ps.setInt(4, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to reschedule appointment", ex);
        }
    }

    public synchronized boolean cancelAppointment(int appointmentId, int patientId) {
        String sql = "UPDATE appointments SET status = 'CANCELLED', updated_at = ? WHERE id = ? AND patient_id = ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, appointmentId);
            ps.setInt(3, patientId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to cancel appointment", ex);
        }
    }

    public synchronized boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update appointment status", ex);
        }
    }

    public synchronized boolean cancelByClinic(int appointmentId, String reason) {
        String sql = "UPDATE appointments SET status = 'CANCELLED', cancellation_reason = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to cancel appointment by clinic", ex);
        }
    }

    public Appointment findById(int appointmentId) {
        String sql = "SELECT id, patient_id, service_id, slot_time, status, created_at, updated_at FROM appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapAppointment(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find appointment", ex);
        }
        return null;
    }

    public List<Appointment> findByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT id, patient_id, service_id, slot_time, status, created_at, updated_at FROM appointments WHERE patient_id = ? ORDER BY slot_time DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAppointment(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list appointments", ex);
        }
        return list;
    }

    public List<Appointment> findUpcomingByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT id, patient_id, service_id, slot_time, status, created_at, updated_at FROM appointments WHERE patient_id = ? AND slot_time > ? AND status <> 'CANCELLED' ORDER BY slot_time ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAppointment(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list upcoming appointments", ex);
        }
        return list;
    }

    private boolean hasDoubleBooking(int patientId, LocalDateTime slotTime, int excludeAppointmentId) {
        String sql = excludeAppointmentId > 0
                ? "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND slot_time = ? AND status <> 'CANCELLED' AND id <> ?"
                : "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND slot_time = ? AND status <> 'CANCELLED'";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setTimestamp(2, Timestamp.valueOf(slotTime));
            if (excludeAppointmentId > 0) {
                ps.setInt(3, excludeAppointmentId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to validate appointment duplication", ex);
        }
        return false;
    }

    public Set<LocalDateTime> findBookedSlotTimesByServiceAndDate(int serviceId, LocalDate date) {
        Set<LocalDateTime> slots = new LinkedHashSet<>();
        String sql = "SELECT slot_time FROM appointments WHERE service_id = ? AND DATE(slot_time) = ? AND status <> 'CANCELLED' ORDER BY slot_time";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    slots.add(rs.getTimestamp("slot_time").toLocalDateTime());
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load booked slots", ex);
        }
        return slots;
    }

    public Map<LocalDateTime, Integer> findBookedSlotCountsByServiceAndDate(int serviceId, LocalDate date) {
        Map<LocalDateTime, Integer> counts = new LinkedHashMap<>();
        String sql = "SELECT slot_time, COUNT(*) AS booking_count FROM appointments WHERE service_id = ? AND DATE(slot_time) = ? AND status <> 'CANCELLED' GROUP BY slot_time ORDER BY slot_time";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getTimestamp("slot_time").toLocalDateTime(), rs.getInt("booking_count"));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load booked slot counts", ex);
        }
        return counts;
    }

    public boolean hasActiveFutureAppointment(int patientId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ? AND slot_time >= ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to validate patient capacity", ex);
        }
        return false;
    }

    public boolean hasSlotConflict(int serviceId, LocalDateTime slotTime, int slotCapacity) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE service_id = ? AND slot_time = ? AND status <> 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setTimestamp(2, Timestamp.valueOf(slotTime));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) >= slotCapacity;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to validate slot capacity", ex);
        }
        return false;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("service_id"),
                rs.getTimestamp("slot_time").toLocalDateTime(),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}

