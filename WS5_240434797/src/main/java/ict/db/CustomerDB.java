package ict.db;

import java.sql.*;
import java.io.*;

public class CustomerDB {
    private String url = "";
    private String username = "";
    private String password = "";

    public CustomerDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException, IOException {
        try {
           Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }

    public void createCustTable() {
        try (Connection cnnct = getConnection(); Statement stmnt = cnnct.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS customer ("
                + "custId varchar(5) NOT NULL, "
                + "name varchar(25) NOT NULL, "
                + "tel varchar(10) NOT NULL, "
                + "age int(11) NOT NULL, "
                + "PRIMARY KEY (custId)"
                + ")";
            stmnt.execute(sql);
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(String custId, String name, String tel, int age) {
        boolean isSuccess = false;
        try (Connection cnnct = getConnection();
             PreparedStatement pStmnt = cnnct.prepareStatement("INSERT INTO CUSTOMER VALUES (?,?,?,?)")) {
            pStmnt.setString(1, custId);
            pStmnt.setString(2, name);
            pStmnt.setString(3, tel);
            pStmnt.setInt(4, age);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) isSuccess = true;
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public CustomerBean queryCustById(String id) {
        CustomerBean cb = null;
        try (Connection cnnct = getConnection();
             PreparedStatement pStmnt = cnnct.prepareStatement("SELECT * FROM CUSTOMER WHERE custId=?")) {
            pStmnt.setString(1, id);
            ResultSet rs = pStmnt.executeQuery();
            if (rs.next()) {
                cb = new CustomerBean();
                cb.setCustId(rs.getString("custId"));
                cb.setName(rs.getString("name"));
                cb.setTel(rs.getString("tel"));
                cb.setAge(rs.getInt("age"));
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cb;
    }

    public CustomerBean queryCustByName(String name) {
        CustomerBean cb = null;
        try (Connection cnnct = getConnection();
             PreparedStatement pStmnt = cnnct.prepareStatement("SELECT * FROM CUSTOMER WHERE name=?")) {
            pStmnt.setString(1, name);
            ResultSet rs = pStmnt.executeQuery();
            if (rs.next()) {
                cb = new CustomerBean();
                cb.setCustId(rs.getString("custId"));
                cb.setName(rs.getString("name"));
                cb.setTel(rs.getString("tel"));
                cb.setAge(rs.getInt("age"));
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cb;
    }

    public CustomerBean queryCustByTel(String tel) {
        CustomerBean cb = null;
        try (Connection cnnct = getConnection();
             PreparedStatement pStmnt = cnnct.prepareStatement("SELECT * FROM CUSTOMER WHERE tel=?")) {
            pStmnt.setString(1, tel);
            ResultSet rs = pStmnt.executeQuery();
            if (rs.next()) {
                cb = new CustomerBean();
                cb.setCustId(rs.getString("custId"));
                cb.setName(rs.getString("name"));
                cb.setTel(rs.getString("tel"));
                cb.setAge(rs.getInt("age"));
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cb;
    }

    public boolean deleteRecord(String custId) {
        boolean isSuccess = false;
        try (Connection cnnct = getConnection();
             PreparedStatement pStmnt = cnnct.prepareStatement("DELETE FROM CUSTOMER WHERE custId=?")) {
            pStmnt.setString(1, custId);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) isSuccess = true;
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public void queryAllCust() {
        try (Connection cnnct = getConnection();
             Statement stmnt = cnnct.createStatement();
             ResultSet rs = stmnt.executeQuery("SELECT * FROM CUSTOMER")) {
            while (rs.next()) {
                System.out.println(rs.getString("custId") + " | "
                        + rs.getString("name") + " | "
                        + rs.getString("tel") + " | "
                        + rs.getInt("age"));
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
