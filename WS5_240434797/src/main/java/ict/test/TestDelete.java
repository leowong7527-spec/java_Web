package ict.test;

import ict.db.CustomerDB;

public class TestDelete {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);

        boolean success = custDb.deleteRecord("2");
        System.out.println(success ? "Record deleted successfully." : "Failed to delete record.");
    }
}
