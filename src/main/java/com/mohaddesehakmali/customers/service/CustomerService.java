package com.mohaddesehakmali.customers.service;

import com.mohaddesehakmali.customers.exception.*;
import com.mohaddesehakmali.customers.model.Customer;
import com.mohaddesehakmali.customers.repository.CustomerRepository;
import com.mohaddesehakmali.customers.validation.CustomerValidator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public Customer addCustomer(String name, String nationalId, String email) {
        String cleanName = clean(name);
        String cleanNationalId = clean(nationalId);
        String cleanEmail = clean(email);
        CustomerValidator.validate(cleanName, cleanNationalId, cleanEmail);
        checkDuplicates(0, cleanNationalId, cleanEmail);
        return repository.create(cleanName, cleanNationalId, cleanEmail);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(int id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer updateCustomer(int id, String name, String nationalId, String email) {
        getCustomerById(id);
        String cleanName = clean(name);
        String cleanNationalId = clean(nationalId);
        String cleanEmail = clean(email);
        CustomerValidator.validate(cleanName, cleanNationalId, cleanEmail);
        checkDuplicates(id, cleanNationalId, cleanEmail);
        return repository.update(new Customer(id, cleanName, cleanNationalId, cleanEmail));
    }

    public void deleteCustomer(int id) {
        if (!repository.deleteById(id)) throw new CustomerNotFoundException(id);
    }

    private void checkDuplicates(int excludedId, String nationalId, String email) {
        for (Customer customer : repository.findAll()) {
            if (customer.id() == excludedId) continue;
            if (customer.nationalId().equals(nationalId)) {
                throw new DuplicateCustomerException("National ID is already registered");
            }
            if (customer.email().toLowerCase(Locale.ROOT).equals(email.toLowerCase(Locale.ROOT))) {
                throw new DuplicateCustomerException("Email is already registered");
            }
        }
    }

    private static String clean(String value) {
        return value == null ? null : value.trim();
    }
}
