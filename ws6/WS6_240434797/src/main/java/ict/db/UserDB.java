package ict.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private static final Logger LOGGER = Logger.getLogger(UserDB.class.getName());

    public UserDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        loadDriver();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL driver not found", ex);
        }
    }

    public void createUserInfoTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS USERINFO (" +
                         "id VARCHAR(5) PRIMARY KEY, " +
                         "username VARCHAR(25), " +
                         "password VARCHAR(25))";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create USERINFO table", ex);
        }
    }

    public boolean addUserInfo(String id, String user, String pwd) {
        boolean result = false;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO USERINFO (id, username, password) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE id=id")) {
            ps.setString(1, id);
            ps.setString(2, user);
            ps.setString(3, pwd);
            result = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert user record", ex);
        }
        return result;
    }

    public boolean isValidUser(String user, String pwd) {
        boolean isValid = false;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM USERINFO WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to validate user credentials", ex);
        }
        return isValid;
    }
}
