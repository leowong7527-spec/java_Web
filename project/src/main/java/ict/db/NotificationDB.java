package ict.db;

import ict.bean.Notification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public NotificationDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public synchronized void create(int userId, String type, String message) {
        String sql = "INSERT INTO notifications (user_id, type, message, created_at, is_read) VALUES (?, ?, ?, ?, 0)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setString(3, message);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create notification", ex);
        }
    }

    public List<Notification> findByUser(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT id, user_id, type, message, created_at, is_read FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("type"),
                            rs.getString("message"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getBoolean("is_read")
                    );
                    list.add(notification);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load notifications", ex);
        }
        return list;
    }

    /**
     * Creates a notification only if no unread notification with the same message already exists
     * for the user. Prevents duplicate reminders on repeated dashboard loads.
     */
    public synchronized void createIfAbsent(int userId, String type, String message) {
        String checkSql = "SELECT id FROM notifications WHERE user_id = ? AND message = ? AND is_read = 0";
        try (Connection conn = getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, userId);
            check.setString(2, message);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to check notification", ex);
        }
        create(userId, type, message);
    }

    public synchronized void markAllRead(int userId) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update notifications", ex);
        }
    }
}


