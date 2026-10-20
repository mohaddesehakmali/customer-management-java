package com.mohaddesehakmali.customers.validation;

import com.mohaddesehakmali.customers.exception.InvalidCustomerException;
import java.util.regex.Pattern;

/** Validates user-provided customer information; no console output or storage access. */
public final class CustomerValidator {
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NATIONAL_ID = Pattern.compile("[0-9]{10}");

    private CustomerValidator() { }

    public static void validate(String name, String nationalId, String email) {
        if (name == null || name.isBlank() || name.length() > 100
                || name.contains("\n") || name.contains("\r")) {
            throw new InvalidCustomerException("Name must be 1–100 characters on one line.");
        }
        if (nationalId == null || !NATIONAL_ID.matcher(nationalId).matches()) {
            throw new InvalidCustomerException("National ID must contain exactly 10 digits.");
        }
        if (email == null || email.length() > 254 || !EMAIL.matcher(email).matches()) {
            throw new InvalidCustomerException("Please enter a valid email address.");
        }
    }
}
