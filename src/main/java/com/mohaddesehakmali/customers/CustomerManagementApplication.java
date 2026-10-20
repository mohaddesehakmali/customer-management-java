package com.mohaddesehakmali.customers;

import com.mohaddesehakmali.customers.exception.*;
import com.mohaddesehakmali.customers.model.Customer;
import com.mohaddesehakmali.customers.repository.FileCustomerRepository;
import com.mohaddesehakmali.customers.service.CustomerService;
import java.nio.file.Path;
import java.util.Scanner;

public final class CustomerManagementApplication {
    private CustomerManagementApplication() { }

    public static void main(String[] args) {
        Path file = Path.of(System.getProperty("customer.data.file", "data/customers.db"));
        try (Scanner scanner = new Scanner(System.in)) {
            CustomerService service = new CustomerService(new FileCustomerRepository(file));
            while (true) {
                System.out.println("\n1. Add customer | 2. List | 3. Find | 4. Update | 5. Delete | 0. Exit");
                int choice = readId(scanner, "Choose an option: ");
                try {
                    switch (choice) {
                        case 1 -> {
                            Customer c = service.addCustomer(
                                    read(scanner, "Name: "), read(scanner, "National ID (10 digits): "),
                                    read(scanner, "Email: "));
                            System.out.println("Created: " + c);
                        }
                        case 2 -> {
                            var customers = service.getAllCustomers();
                            if (customers.isEmpty()) System.out.println("No customers found.");
                            else customers.forEach(System.out::println);
                        }
                        case 3 -> System.out.println(service.getCustomerById(readId(scanner, "Customer ID: ")));
                        case 4 -> {
                            int id = readId(scanner, "Customer ID: ");
                            Customer updated = service.updateCustomer(id,
                                    read(scanner, "Name: "), read(scanner, "National ID (10 digits): "),
                                    read(scanner, "Email: "));
                            System.out.println("Updated: " + updated);
                        }
                        case 5 -> {
                            service.deleteCustomer(readId(scanner, "Customer ID: "));
                            System.out.println("Customer deleted.");
                        }
                        case 0 -> { return; }
                        default -> System.out.println("Please choose a listed option.");
                    }
                } catch (InvalidCustomerException | DuplicateCustomerException |
                         CustomerNotFoundException | CustomerStorageException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (CustomerStorageException e) {
            System.err.println("Failed to open customer storage: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String read(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readId(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
