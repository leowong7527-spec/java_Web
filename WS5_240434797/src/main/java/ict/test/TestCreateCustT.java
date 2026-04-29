package ict.test;

import ict.db.CustomerDB;

public class TestCreateCustT {
    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);
        custDb.createCustTable();
        System.out.println("Customer table created successfully.");
    }
}
