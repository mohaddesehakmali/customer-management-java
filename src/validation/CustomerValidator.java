package validation;

import exception.InvalidCustomerException;

public class CustomerValidator {

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCustomerException("Name cannot be empty.");
        }
    }

    public static void validateNationalId(String nationalId) {
        if (nationalId == null || !nationalId.matches("\\d{10}")) {
            throw new InvalidCustomerException("National ID must be exactly 10 digits.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidCustomerException("Invalid email format.");
        }
    }

    public static void validateCustomer(String name, String nationalId, String email) {
        validateName(name);
        validateNationalId(nationalId);
        validateEmail(email);
    }
}