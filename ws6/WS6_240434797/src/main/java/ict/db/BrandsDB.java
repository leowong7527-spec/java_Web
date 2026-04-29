package ict.db;

import ict.bean.Brand;
import ict.bean.Phone;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrandsDB {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private static final Logger LOGGER = Logger.getLogger(BrandsDB.class.getName());
    private static final Map<String, Boolean> INITIALIZED_DBS = new ConcurrentHashMap<>();

    public BrandsDB(String dbUrl, String dbUser, String dbPassword) {
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

    public void initialize() {
        String key = initializationKey();
        INITIALIZED_DBS.computeIfAbsent(key, k -> {
            createPhoneTable();
            seedSampleData();
            return Boolean.TRUE;
        });
    }

    private String initializationKey() {
        return dbUrl + "::" + dbUser;
    }

    public void createPhoneTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS PHONE (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "brand VARCHAR(50), " +
                         "name VARCHAR(100), " +
                         "img VARCHAR(255), " +
                         "price DOUBLE, " +
                         "UNIQUE KEY uk_phone_name (name))";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create PHONE table", ex);
        }
    }

    public void seedSampleData() {
        String insertSql = "INSERT INTO PHONE (brand, name, img, price) VALUES (?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE brand=VALUES(brand), img=VALUES(img), price=VALUES(price)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            addPhone(ps, "Apple", "iPhone 15", "img/iphone15.jpg", 8999);
            addPhone(ps, "Samsung", "Galaxy S24", "img/galaxy-s24.jpg", 7999);
            addPhone(ps, "Google", "Pixel 8", "img/pixel8.jpg", 6999);
            addPhone(ps, "Apple", "iPhone 14", "img/iphone14.jpg", 6999);
            ps.executeBatch();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to seed sample phone data", ex);
        }
    }

    private void addPhone(PreparedStatement ps, String brand, String name, String img, double price) throws SQLException {
        ps.setString(1, brand);
        ps.setString(2, name);
        ps.setString(3, img);
        ps.setDouble(4, price);
        ps.addBatch();
    }

    public ArrayList<Brand> getAllBrands() {
        ArrayList<Brand> brands = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand FROM PHONE")) {
            while (rs.next()) {
                brands.add(new Brand(rs.getString("brand")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve brands", ex);
        }
        return brands;
    }

    public ArrayList<Phone> getPhonesByBrand(String brand) {
        ArrayList<Phone> phones = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT name, img, price FROM PHONE WHERE brand=?")) {
            ps.setString(1, brand);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    phones.add(new Phone(rs.getString("name"), rs.getString("img"), rs.getDouble("price")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve phones for brand: " + brand, ex);
        }
        return phones;
    }
}
