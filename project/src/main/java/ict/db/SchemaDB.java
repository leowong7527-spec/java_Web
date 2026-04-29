package ict.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;

public final class SchemaDB {
    private static boolean initialized = false;
    private static final LocalTime DEFAULT_OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_CLOSE_TIME = LocalTime.of(17, 0);

    private SchemaDB() {
    }

    public static synchronized void initialize(String dbUrl, String dbUser, String dbPassword) {
        if (initialized) {
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            createTables(conn);
            seedData(conn);
            initialized = true;
        } catch (SQLException ex) {
            if (isUnknownDatabaseError(ex)) {
                createDatabaseThenInitialize(dbUrl, dbUser, dbPassword);
                initialized = true;
                return;
            }
            throw new RuntimeException("Failed to initialize schema", ex);
        }
    }

    private static boolean isUnknownDatabaseError(SQLException ex) {
        String msg = ex.getMessage();
        return msg != null && msg.toLowerCase().contains("unknown database");
    }

    private static void createDatabaseThenInitialize(String dbUrl, String dbUser, String dbPassword) {
        String dbName = extractDatabaseName(dbUrl);
        String baseUrl = extractBaseUrl(dbUrl);

        if (!dbName.matches("[A-Za-z0-9_]+")) {
            throw new RuntimeException("Unsafe database name: " + dbName);
        }

        String createDbSql = "CREATE DATABASE IF NOT EXISTS `" + dbName + "` "
                + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection baseConn = DriverManager.getConnection(baseUrl, dbUser, dbPassword);
             Statement stmt = baseConn.createStatement()) {
            stmt.executeUpdate(createDbSql);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create database", ex);
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            createTables(conn);
            seedData(conn);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to initialize schema after database creation", ex);
        }
    }

    private static String extractDatabaseName(String jdbcUrl) {
        int protocolEnd = jdbcUrl.indexOf("//");
        int slash = jdbcUrl.indexOf('/', protocolEnd >= 0 ? protocolEnd + 2 : 0);
        if (slash < 0 || slash + 1 >= jdbcUrl.length()) {
            throw new RuntimeException("Invalid JDBC URL, database name missing: " + jdbcUrl);
        }

        String dbPart = jdbcUrl.substring(slash + 1);
        int q = dbPart.indexOf('?');
        if (q >= 0) {
            dbPart = dbPart.substring(0, q);
        }
        return dbPart;
    }

    private static String extractBaseUrl(String jdbcUrl) {
        int protocolEnd = jdbcUrl.indexOf("//");
        int slash = jdbcUrl.indexOf('/', protocolEnd >= 0 ? protocolEnd + 2 : 0);
        if (slash < 0) {
            throw new RuntimeException("Invalid JDBC URL: " + jdbcUrl);
        }
        return jdbcUrl.substring(0, slash + 1);
    }

    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password VARCHAR(100) NOT NULL,"
                    + "full_name VARCHAR(100) NOT NULL,"
                    + "phone VARCHAR(30),"
                    + "email VARCHAR(100),"
                    + "role VARCHAR(20) NOT NULL,"
                    + "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS policy_settings ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "policy_key VARCHAR(100) NOT NULL UNIQUE,"
                    + "policy_value VARCHAR(255) NOT NULL,"
                    + "description VARCHAR(255),"
                    + "updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS audit_log ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "user_id INT NOT NULL,"
                    + "action_type VARCHAR(50) NOT NULL,"
                    + "target_type VARCHAR(50),"
                    + "target_id INT,"
                    + "description VARCHAR(500),"
                    + "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "INDEX idx_audit_user (user_id),"
                    + "CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clinic_service ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "clinic_name VARCHAR(100) NOT NULL,"
                    + "service_name VARCHAR(100) NOT NULL,"
                    + "daily_quota INT NOT NULL,"
                    + "slot_capacity INT NOT NULL DEFAULT 1,"
                    + "opening_time TIME NOT NULL DEFAULT '09:00:00',"
                    + "closing_time TIME NOT NULL DEFAULT '17:00:00'"
                    + ")");

                stmt.executeUpdate("ALTER TABLE clinic_service ADD COLUMN IF NOT EXISTS slot_capacity INT NOT NULL DEFAULT 1");
                stmt.executeUpdate("ALTER TABLE clinic_service ADD COLUMN IF NOT EXISTS opening_time TIME NOT NULL DEFAULT '09:00:00'");
                stmt.executeUpdate("ALTER TABLE clinic_service ADD COLUMN IF NOT EXISTS closing_time TIME NOT NULL DEFAULT '17:00:00'");
                stmt.executeUpdate("UPDATE clinic_service SET slot_capacity = COALESCE(slot_capacity, 1), opening_time = COALESCE(opening_time, '09:00:00'), closing_time = COALESCE(closing_time, '17:00:00')");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS appointments ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "patient_id INT NOT NULL,"
                    + "service_id INT NOT NULL,"
                    + "slot_time DATETIME NOT NULL,"
                    + "status VARCHAR(30) NOT NULL,"
                    + "created_at DATETIME NOT NULL,"
                    + "updated_at DATETIME NOT NULL,"
                    + "INDEX idx_appt_patient (patient_id),"
                    + "INDEX idx_appt_service (service_id),"
                    + "CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES users(id),"
                    + "CONSTRAINT fk_appt_service FOREIGN KEY (service_id) REFERENCES clinic_service(id)"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS queue_entries ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "patient_id INT NOT NULL,"
                    + "service_id INT NOT NULL,"
                    + "queue_date DATE NOT NULL,"
                    + "queue_number INT NOT NULL,"
                    + "status VARCHAR(30) NOT NULL,"
                    + "joined_at DATETIME NOT NULL,"
                    + "UNIQUE KEY uq_queue_slot (service_id, queue_date, queue_number),"
                    + "INDEX idx_queue_patient (patient_id),"
                    + "CONSTRAINT fk_queue_patient FOREIGN KEY (patient_id) REFERENCES users(id),"
                    + "CONSTRAINT fk_queue_service FOREIGN KEY (service_id) REFERENCES clinic_service(id)"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "user_id INT NOT NULL,"
                    + "type VARCHAR(30) NOT NULL,"
                    + "message VARCHAR(255) NOT NULL,"
                    + "created_at DATETIME NOT NULL,"
                    + "is_read TINYINT(1) NOT NULL DEFAULT 0,"
                    + "INDEX idx_noti_user (user_id),"
                    + "CONSTRAINT fk_noti_user FOREIGN KEY (user_id) REFERENCES users(id)"
                    + ")");
        }
    }

    private static void seedData(Connection conn) throws SQLException {
        seedUser(conn, "admin", "admin123", "System Administrator", "10000000", "admin@cchc.hk", "ADMIN");
        seedUser(conn, "staff", "staff123", "Lee Ming Tan", "12345678", "LeeMingTan.cchc.edu.hk", "STAFF");
        seedUser(conn, "patient", "patient123", "Chan Tai Man", "87654321", "ChanTaiMan@gmail.com", "PATIENT");
        seedPolicySetting(conn, "max_active_bookings_per_patient", "1", "Maximum number of active future bookings per patient");
        seedPolicySetting(conn, "cancellation_cutoff_hours", "24", "Hours before appointment that cancellation is still allowed");
        seedPolicySetting(conn, "queue_enabled", "true", "Whether walk-in queue is globally enabled");

        seedService(conn, "Central Clinic", "General Consultation", 40, 1, DEFAULT_OPEN_TIME, DEFAULT_CLOSE_TIME);
        seedService(conn, "Central Clinic", "Vaccination", 25, 1, DEFAULT_OPEN_TIME, DEFAULT_CLOSE_TIME);
        seedService(conn, "North Point Clinic", "Dental Check", 20, 1, DEFAULT_OPEN_TIME, DEFAULT_CLOSE_TIME);
    }

    private static void seedUser(Connection conn, String username, String password, String fullName, String phone, String email, String role) throws SQLException {
        String checkSql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setString(1, username);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }
        }

        String insertSql = "INSERT INTO users (username, password, full_name, phone, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, role);
            ps.executeUpdate();
        }
    }

    private static void seedPolicySetting(Connection conn, String key, String value, String description) throws SQLException {
        String checkSql = "SELECT id FROM policy_settings WHERE policy_key = ?";
        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setString(1, key);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }
        }
        String insertSql = "INSERT INTO policy_settings (policy_key, policy_value, description) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, key);
            ps.setString(2, value);
            ps.setString(3, description);
            ps.executeUpdate();
        }
    }

    private static void seedService(Connection conn, String clinicName, String serviceName, int dailyQuota, int slotCapacity, LocalTime openingTime, LocalTime closingTime) throws SQLException {
        String checkSql = "SELECT id FROM clinic_service WHERE clinic_name = ? AND service_name = ?";
        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setString(1, clinicName);
            check.setString(2, serviceName);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement update = conn.prepareStatement("UPDATE clinic_service SET daily_quota = ?, slot_capacity = ?, opening_time = ?, closing_time = ? WHERE id = ?")) {
                        update.setInt(1, dailyQuota);
                        update.setInt(2, slotCapacity);
                        update.setTime(3, Time.valueOf(openingTime));
                        update.setTime(4, Time.valueOf(closingTime));
                        update.setInt(5, rs.getInt("id"));
                        update.executeUpdate();
                    }
                    return;
                }
            }
        }

        String insertSql = "INSERT INTO clinic_service (clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, clinicName);
            ps.setString(2, serviceName);
            ps.setInt(3, dailyQuota);
            ps.setInt(4, slotCapacity);
            ps.setTime(5, Time.valueOf(openingTime));
            ps.setTime(6, Time.valueOf(closingTime));
            ps.executeUpdate();
        }
    }
}
