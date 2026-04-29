package ict.test;

import ict.db.CustomerDB;

public class TestAddRecord {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);

        // Insert Peter
        boolean success1 = custDb.addRecord("1", "Peter", "12345688", 18);
        System.out.println(success1 ? "Peter added successfully." : "Failed to add Peter.");

        // Insert Nancy
        boolean success2 = custDb.addRecord("2", "Nancy", "12345678", 21);
        System.out.println(success2 ? "Nancy added successfully." : "Failed to add Nancy.");
    }
}
