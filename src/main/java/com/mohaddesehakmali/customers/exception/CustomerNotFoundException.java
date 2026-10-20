package com.mohaddesehakmali.customers.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(int id) {
        super("Customer not found with ID: " + id);
    }
}
