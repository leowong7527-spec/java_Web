package ict.db;

import java.sql.*;

public class StaffDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public StaffDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public boolean reportIssue(int clinicId, String type, String desc, int staffId) {
        String sql = "INSERT INTO operational_issues (clinic_id, issue_type, description, reported_by) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clinicId);
            ps.setString(2, type);
            ps.setString(3, desc);
            ps.setInt(4, staffId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }
}