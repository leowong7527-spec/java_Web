package ict.db;

import ict.bean.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public UserDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public synchronized User registerPatient(String username, String password, String fullName, String phone, String email) {
        String normalized = username == null ? "" : username.trim().toLowerCase();
        if (normalized.isEmpty()) {
            return null;
        }

        String checkSql = "SELECT id FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, full_name, phone, email, role) VALUES (?, ?, ?, ?, ?, 'PATIENT')";

        try (Connection conn = getConnection()) {
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, normalized);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        return null;
                    }
                }
            }

            try (PreparedStatement insert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insert.setString(1, normalized);
                insert.setString(2, password);
                insert.setString(3, fullName);
                insert.setString(4, phone);
                insert.setString(5, email);
                insert.executeUpdate();

                try (ResultSet rs = insert.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        return new User(id, normalized, password, fullName, phone, email, "PATIENT");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to register patient", ex);
        }
        return null;
    }

    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        String sql = "SELECT id, username, password, full_name, phone, email, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim().toLowerCase());
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to authenticate", ex);
        }
        return null;
    }

    public User findById(int id) {
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

    public synchronized void updateProfile(int userId, String fullName, String phone, String email) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update profile", ex);
        }
    }

    public synchronized boolean updatePassword(int userId, String oldPassword, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.setString(3, oldPassword);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update password", ex);
        }
    }

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
}


