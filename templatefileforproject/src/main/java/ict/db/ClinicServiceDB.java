package ict.db;

import ict.bean.ClinicService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

public class ClinicServiceDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public ClinicServiceDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        SchemaDB.initialize(dbUrl, dbUser, dbPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public List<ClinicService> findAll() {
        List<ClinicService> list = new ArrayList<>();
        String sql = "SELECT id, clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time FROM clinic_service ORDER BY clinic_name, service_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ClinicService(
                        rs.getInt("id"),
                        rs.getString("clinic_name"),
                        rs.getString("service_name"),
                    rs.getInt("daily_quota"),
                    rs.getInt("slot_capacity"),
                    readTime(rs, "opening_time", LocalTime.of(9, 0)),
                    readTime(rs, "closing_time", LocalTime.of(17, 0))
                ));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load clinic services", ex);
        }
        return list;
    }

    public ClinicService findById(int id) {
        String sql = "SELECT id, clinic_name, service_name, daily_quota, slot_capacity, opening_time, closing_time FROM clinic_service WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClinicService(
                            rs.getInt("id"),
                            rs.getString("clinic_name"),
                            rs.getString("service_name"),
                            rs.getInt("daily_quota"),
                            rs.getInt("slot_capacity"),
                            readTime(rs, "opening_time", LocalTime.of(9, 0)),
                            readTime(rs, "closing_time", LocalTime.of(17, 0))
                    );
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load clinic service", ex);
        }
        return null;
    }

    private LocalTime readTime(ResultSet rs, String column, LocalTime fallback) throws SQLException {
        java.sql.Time time = rs.getTime(column);
        return time == null ? fallback : time.toLocalTime();
    }
}


