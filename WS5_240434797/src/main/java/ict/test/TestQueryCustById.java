package ict.test;

import ict.db.CustomerDB;
import ict.db.CustomerBean;

public class TestQueryCustById {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);

        CustomerBean cb = custDb.queryCustById("1");
        if (cb != null) {
            System.out.println("Customer found: " 
                + cb.getCustId() + " | "
                + cb.getName() + " | "
                + cb.getTel() + " | "
                + cb.getAge());
        } else {
            System.out.println("No customer found with that ID.");
        }
    }
}
