package com.mohaddesehakmali.customers.model;

/** Immutable customer details with a stable database-style ID. */
public record Customer(int id, String name, String nationalId, String email) { }
