package ict.db;

import ict.bean.QueueEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueueDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public QueueDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public synchronized QueueEntry joinTodayQueue(int patientId, int serviceId) {
        LocalDate today = LocalDate.now();
        String existingSql = "SELECT id, patient_id, service_id, queue_date, queue_number, status, joined_at FROM queue_entries WHERE patient_id = ? AND service_id = ? AND queue_date = ? AND (status = 'WAITING' OR status = 'CALLED') ORDER BY queue_number LIMIT 1";
        String maxSql = "SELECT COALESCE(MAX(queue_number), 0) FROM queue_entries WHERE service_id = ? AND queue_date = ?";
        String insertSql = "INSERT INTO queue_entries (patient_id, service_id, queue_date, queue_number, status, joined_at) VALUES (?, ?, ?, ?, 'WAITING', ?)";

        try (Connection conn = getConnection()) {
            try (PreparedStatement existingPs = conn.prepareStatement(existingSql)) {
                existingPs.setInt(1, patientId);
                existingPs.setInt(2, serviceId);
                existingPs.setDate(3, java.sql.Date.valueOf(today));
                try (ResultSet rs = existingPs.executeQuery()) {
                    if (rs.next()) {
                        return mapQueueEntry(rs);
                    }
                }
            }

            int maxQueue = 0;
            try (PreparedStatement maxPs = conn.prepareStatement(maxSql)) {
                maxPs.setInt(1, serviceId);
                maxPs.setDate(2, java.sql.Date.valueOf(today));
                try (ResultSet rs = maxPs.executeQuery()) {
                    if (rs.next()) {
                        maxQueue = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement insertPs = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertPs.setInt(1, patientId);
                insertPs.setInt(2, serviceId);
                insertPs.setDate(3, java.sql.Date.valueOf(today));
                insertPs.setInt(4, maxQueue + 1);
                insertPs.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
                insertPs.executeUpdate();

                try (ResultSet rs = insertPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        return findById(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to join queue", ex);
        }
        return null;
    }

    public List<QueueEntry> findTodayByPatient(int patientId) {
        LocalDate today = LocalDate.now();
        List<QueueEntry> list = new ArrayList<>();
        String sql = "SELECT id, patient_id, service_id, queue_date, queue_number, status, joined_at FROM queue_entries WHERE patient_id = ? AND queue_date = ? ORDER BY queue_number";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setDate(2, java.sql.Date.valueOf(today));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapQueueEntry(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to list queue entries", ex);
        }
        return list;
    }

    public int estimateWaitMinutes(int serviceId, int queueNumber) {
        LocalDate today = LocalDate.now();
        String sql = "SELECT COUNT(*) FROM queue_entries WHERE service_id = ? AND queue_date = ? AND queue_number < ? AND status = 'WAITING'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, java.sql.Date.valueOf(today));
            ps.setInt(3, queueNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) * 10;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to estimate wait time", ex);
        }
        return 0;
    }

    public synchronized boolean updateQueueStatus(int queueId, String status) {
        String sql = "UPDATE queue_entries SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, queueId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update queue status", ex);
        }
    }

    private QueueEntry findById(int id) {
        String sql = "SELECT id, patient_id, service_id, queue_date, queue_number, status, joined_at FROM queue_entries WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapQueueEntry(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to find queue entry", ex);
        }
        return null;
    }

    private QueueEntry mapQueueEntry(ResultSet rs) throws SQLException {
        return new QueueEntry(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("service_id"),
                rs.getDate("queue_date").toLocalDate(),
                rs.getInt("queue_number"),
                rs.getString("status"),
                rs.getTimestamp("joined_at").toLocalDateTime()
        );
    }
}


