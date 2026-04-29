package ict.db;

import ict.bean.ClinicService;
import ict.bean.User;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public AdminDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    // ─── User Management ────────────────────────────────────────────────────────

    public List<User> listAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, full_name, phone, email, role FROM users ORDER BY role, username";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list users", ex);
        }
        return list;
    }

    public User findUserById(int id) {
        String sql = "SELECT id, username, password, full_name, phone, email, role FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find user", ex);
        }
        return null;
    }

    public synchronized boolean createUser(String username, String password, String fullName,
                                           String phone, String email, String role) {
        String checkSql = "SELECT id FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, full_name, phone, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, username.trim().toLowerCase());
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, username.trim().toLowerCase());
                ps.setString(2, password);
                ps.setString(3, fullName);
                ps.setString(4, phone);
                ps.setString(5, email);
                ps.setString(6, role);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create user", ex);
        }
    }

    public synchronized boolean updateUser(int id, String fullName, String phone, String email, String role, String newPassword) {
        try (Connection conn = getConnection()) {
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                String sql = "UPDATE users SET full_name = ?, phone = ?, email = ?, role = ?, password = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, fullName);
                    ps.setString(2, phone);
                    ps.setString(3, email);
                    ps.setString(4, role);
                    ps.setString(5, newPassword);
                    ps.setInt(6, id);
                    return ps.executeUpdate() > 0;
                }
            } else {
                String sql = "UPDATE users SET full_name = ?, phone = ?, email = ?, role = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, fullName);
                    ps.setString(2, phone);
                    ps.setString(3, email);
                    ps.setString(4, role);
                    ps.setInt(5, id);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update user", ex);
        }
    }

    public synchronized boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to delete user", ex);
        }
    }

    // ─── Clinic Service Management ───────────────────────────────────────────────

    public List<ClinicService> listClinicServices() {
        List<ClinicService> list = new ArrayList<>();
        String sql = "SELECT id, clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time "
                + "FROM clinic_service ORDER BY clinic_name, service_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapService(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list services", ex);
        }
        return list;
    }

    public ClinicService findServiceById(int id) {
        String sql = "SELECT id, clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time "
                + "FROM clinic_service WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapService(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find service", ex);
        }
        return null;
    }

    public synchronized boolean createClinicService(String clinicName, String serviceName,
                                                    int dailyQuota, int slotCapacity,
                                                    LocalTime openingTime, LocalTime closingTime) {
        String sql = "INSERT INTO clinic_service (clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clinicName);
            ps.setString(2, serviceName);
            ps.setInt(3, dailyQuota);
            ps.setInt(4, slotCapacity);
            ps.setTime(5, Time.valueOf(openingTime));
            ps.setTime(6, Time.valueOf(closingTime));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create service", ex);
        }
    }

    public synchronized boolean updateClinicService(int id, String clinicName, String serviceName,
                                                    int dailyQuota, int slotCapacity,
                                                    LocalTime openingTime, LocalTime closingTime) {
        String sql = "UPDATE clinic_service SET clinic_name = ?, service_name = ?, daily_quota = ?, "
                + "slot_capacity = ?, opening_time = ?, closing_time = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clinicName);
            ps.setString(2, serviceName);
            ps.setInt(3, dailyQuota);
            ps.setInt(4, slotCapacity);
            ps.setTime(5, Time.valueOf(openingTime));
            ps.setTime(6, Time.valueOf(closingTime));
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update service", ex);
        }
    }

    public synchronized boolean deleteClinicService(int id) {
        String sql = "DELETE FROM clinic_service WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to delete service", ex);
        }
    }

    // ─── Reporting ───────────────────────────────────────────────────────────────

    /**
     * Returns per-service booking statistics for the given year/month.
     * Each map has keys: clinic_name, service_name, total, completed, no_show, cancelled, booked, daily_quota
     */
    public List<Map<String, Object>> getMonthlyReport(int year, int month) {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT cs.clinic_name, cs.service_name, cs.daily_quota, "
                + "COUNT(*) AS total, "
                + "SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed, "
                + "SUM(CASE WHEN a.status IN ('NOSHOW','NO-SHOW') THEN 1 ELSE 0 END) AS no_show, "
                + "SUM(CASE WHEN a.status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled, "
                + "SUM(CASE WHEN a.status IN ('BOOKED','RESCHEDULED','ARRIVED') THEN 1 ELSE 0 END) AS active "
                + "FROM appointments a "
                + "JOIN clinic_service cs ON a.service_id = cs.id "
                + "WHERE YEAR(a.slot_time) = ? AND MONTH(a.slot_time) = ? "
                + "GROUP BY cs.id, cs.clinic_name, cs.service_name, cs.daily_quota "
                + "ORDER BY cs.clinic_name, cs.service_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("clinic_name", rs.getString("clinic_name"));
                    row.put("service_name", rs.getString("service_name"));
                    row.put("daily_quota", rs.getInt("daily_quota"));
                    row.put("total", rs.getInt("total"));
                    row.put("completed", rs.getInt("completed"));
                    row.put("no_show", rs.getInt("no_show"));
                    row.put("cancelled", rs.getInt("cancelled"));
                    row.put("active", rs.getInt("active"));
                    rows.add(row);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to generate monthly report", ex);
        }
        return rows;
    }

    /** Overall counts for the admin dashboard summary card. */
    public Map<String, Integer> getDashboardCounts() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role='PATIENT'")) {
                    counts.put("patients", rs.next() ? rs.getInt(1) : 0);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role='STAFF'")) {
                    counts.put("staff", rs.next() ? rs.getInt(1) : 0);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM clinic_service")) {
                    counts.put("services", rs.next() ? rs.getInt(1) : 0);
                }
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM appointments WHERE DATE(slot_time) = CURDATE()")) {
                    counts.put("today_appointments", rs.next() ? rs.getInt(1) : 0);
                }
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT COUNT(*) FROM queue_entries WHERE queue_date = CURDATE() AND status = 'WAITING'")) {
                    counts.put("today_queue", rs.next() ? rs.getInt(1) : 0);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load dashboard counts", ex);
        }
        return counts;
    }

    // ─── Policy Settings ─────────────────────────────────────────────────────────

    public List<Map<String, String>> listPolicySettings() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT policy_key, policy_value, description FROM policy_settings ORDER BY policy_key";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("key", rs.getString("policy_key"));
                row.put("value", rs.getString("policy_value"));
                row.put("description", rs.getString("description"));
                list.add(row);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list policy settings", ex);
        }
        return list;
    }

    public synchronized boolean updatePolicySetting(String key, String value) {
        String sql = "UPDATE policy_settings SET policy_value = ? WHERE policy_key = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update policy setting", ex);
        }
    }

    // ─── Audit Log ───────────────────────────────────────────────────────────────

    public synchronized void addAuditLog(int userId, String actionType, String targetType, int targetId, String description) {
        String sql = "INSERT INTO audit_log (user_id, action_type, target_type, target_id, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, actionType);
            ps.setString(3, targetType);
            ps.setInt(4, targetId);
            ps.setString(5, description);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add audit log", ex);
        }
    }

    public List<Map<String, Object>> listAuditLogs(int limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT al.id, u.username, al.action_type, al.target_type, al.target_id, al.description, al.created_at "
                + "FROM audit_log al LEFT JOIN users u ON al.user_id = u.id "
                + "ORDER BY al.created_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("username", rs.getString("username"));
                    row.put("action_type", rs.getString("action_type"));
                    row.put("target_type", rs.getString("target_type"));
                    row.put("target_id", rs.getInt("target_id"));
                    row.put("description", rs.getString("description"));
                    row.put("created_at", rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(row);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list audit logs", ex);
        }
        return list;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("role")
        );
    }

    private ClinicService mapService(ResultSet rs) throws SQLException {
        LocalTime open = rs.getTime("opening_time") != null
                ? rs.getTime("opening_time").toLocalTime() : LocalTime.of(9, 0);
        LocalTime close = rs.getTime("closing_time") != null
                ? rs.getTime("closing_time").toLocalTime() : LocalTime.of(17, 0);
        return new ClinicService(
                rs.getInt("id"),
                rs.getString("clinic_name"),
                rs.getString("service_name"),
                rs.getInt("daily_quota"),
                rs.getInt("slot_capacity"),
                open, close
        );
    }
}
