package ict.db;

import ict.bean.CustomerBean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDB {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public CustomerDB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }

        ensureSchema();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private void ensureSchema() {
        try (Connection conn = getConnection()) {
            createCustomerTableIfMissing(conn);
            seedSampleCustomers(conn);
        } catch (SQLException ex) {
            if (isUnknownDatabaseError(ex)) {
                createDatabaseThenInitialize();
                return;
            }
            throw new RuntimeException("Failed to initialize customer schema", ex);
        }
    }

    private boolean isUnknownDatabaseError(SQLException ex) {
        String message = ex.getMessage();
        return message != null && message.toLowerCase().contains("unknown database");
    }

    private void createDatabaseThenInitialize() {
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
            throw new RuntimeException("Failed to create database " + dbName, ex);
        }

        try (Connection conn = getConnection()) {
            createCustomerTableIfMissing(conn);
            seedSampleCustomers(conn);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to initialize tables in database " + dbName, ex);
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
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

        if (dbPart.isEmpty()) {
            throw new RuntimeException("Invalid JDBC URL, database name missing: " + jdbcUrl);
        }

        return dbPart;
    }

    private String extractBaseUrl(String jdbcUrl) {
        int protocolEnd = jdbcUrl.indexOf("//");
        int slash = jdbcUrl.indexOf('/', protocolEnd >= 0 ? protocolEnd + 2 : 0);
        if (slash < 0) {
            throw new RuntimeException("Invalid JDBC URL: " + jdbcUrl);
        }
        return jdbcUrl.substring(0, slash + 1);
    }

    private void createCustomerTableIfMissing(Connection conn) throws SQLException {
        String createSql = "CREATE TABLE IF NOT EXISTS customer ("
                + "custId VARCHAR(10) PRIMARY KEY,"
                + "name VARCHAR(50),"
                + "tel VARCHAR(20),"
                + "age INT"
                + ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createSql);
        }

        if (!hasColumn(conn, "customer", "custId")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS customer");
                stmt.executeUpdate(createSql);
            }
        }
    }

    private boolean hasColumn(Connection conn, String table, String column) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns(conn.getCatalog(), null, table, column)) {
            return rs.next();
        }
    }

    private void seedSampleCustomers(Connection conn) throws SQLException {
        String existsSql = "SELECT COUNT(*) FROM customer WHERE custId = ?";
        String insertSql = "INSERT INTO customer (custId, name, tel, age) VALUES (?, ?, ?, ?)";

        seedIfMissing(conn, existsSql, insertSql, "1", "Peter", "12345688", 22);
        seedIfMissing(conn, existsSql, insertSql, "2", "Nancy", "12345678", 21);
    }

    private void seedIfMissing(Connection conn, String existsSql, String insertSql,
                               String custId, String name, String tel, int age) throws SQLException {
        boolean exists;
        try (PreparedStatement existsStmt = conn.prepareStatement(existsSql)) {
            existsStmt.setString(1, custId);
            try (ResultSet rs = existsStmt.executeQuery()) {
                rs.next();
                exists = rs.getInt(1) > 0;
            }
        }

        if (!exists) {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, custId);
                insertStmt.setString(2, name);
                insertStmt.setString(3, tel);
                insertStmt.setInt(4, age);
                insertStmt.executeUpdate();
            }
        }
    }

    public ArrayList<CustomerBean> queryCust() throws SQLException {
        ArrayList<CustomerBean> list = new ArrayList<>();
        String sql = "SELECT custId, name, tel, age FROM customer";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public ArrayList<CustomerBean> queryCustByName(String name) throws SQLException {
        ArrayList<CustomerBean> list = new ArrayList<>();
        String sql = "SELECT custId, name, tel, age FROM customer WHERE name LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public CustomerBean queryCustByID(String id) throws SQLException {
        String sql = "SELECT custId, name, tel, age FROM customer WHERE custId = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void addRecord(String id, String name, String tel, int age) throws SQLException {
        String sql = "INSERT INTO customer VALUES(?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, tel);
            ps.setInt(4, age);
            ps.executeUpdate();
        }
    }

    public void editRecord(String id, String name, String tel, int age) throws SQLException {
        String sql = "UPDATE customer SET name = ?, tel = ?, age = ? WHERE custId = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, tel);
            ps.setInt(3, age);
            ps.setString(4, id);
            ps.executeUpdate();
        }
    }

    public void delRecord(String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE custId = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private CustomerBean mapRow(ResultSet rs) throws SQLException {
        return new CustomerBean(
                rs.getString("custId"),
                rs.getString("name"),
                rs.getString("tel"),
                rs.getInt("age")
        );
    }
}
