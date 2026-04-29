package ict.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomersBean implements Serializable {

    private ArrayList<CustomerBean> customers;

    // default constructor initializes the list
    public CustomersBean() {
        customers = new ArrayList<>();
    }

    // getter
    public ArrayList<CustomerBean> getCustomers() {
        return customers;
    }

    // setter
    public void setCustomers(ArrayList<CustomerBean> customers) {
        this.customers = customers;
    }

    // add a single customer
    public void addCustomer(CustomerBean customer) {
        this.customers.add(customer);
    }
}
