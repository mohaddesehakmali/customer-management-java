package com.mohaddesehakmali.customers.repository;

import com.mohaddesehakmali.customers.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    List<Customer> findAll();
    Optional<Customer> findById(int id);
    Customer create(String name, String nationalId, String email);
    Customer update(Customer customer);
    boolean deleteById(int id);
}
