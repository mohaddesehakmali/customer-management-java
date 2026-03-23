package service;

import model.Customer;
import validation.CustomerValidator;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final List<Customer> customers = new ArrayList<>();
    private int idCounter = 1;

    public void addCustomer(String name, String nationalId, String email) {
        CustomerValidator.validateCustomer(name, nationalId, email);

        Customer customer = new Customer(idCounter++, name, nationalId, email);
        customers.add(customer);
        System.out.println("Customer added successfully.");
    }

    public void viewAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public void searchCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                System.out.println(customer);
                return;
            }
        }
        System.out.println("Customer not found.");
    }

    public void updateCustomer(int id, String name, String nationalId, String email) {
        CustomerValidator.validateCustomer(name, nationalId, email);

        for (Customer customer : customers) {
            if (customer.getId() == id) {
                customer.setName(name);
                customer.setNationalId(nationalId);
                customer.setEmail(email);
                System.out.println("Customer updated successfully.");
                return;
            }
        }
        System.out.println("Customer not found.");
    }

    public void deleteCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                customers.remove(customer);
                System.out.println("Customer deleted successfully.");
                return;
            }
        }
        System.out.println("Customer not found.");
    }
}