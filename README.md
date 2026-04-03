#  Customer Management System (Java)

A console-based Customer Management System built with Java, demonstrating CRUD operations, validation, and exception handling.

---

## Features

- Add, View, Search, Update, Delete customers  
- Input validation (name, national ID, email)  
- Custom exception handling  

---

## Project Structure

```
src/
 ├── exception/
 ├── model/
 ├── service/
 ├── util/
 ├── validation/
 └── main.java
```

---

## Code Examples

### Add Customer
```java
public void addCustomer(String name, String nationalId, String email) {
    CustomerValidator.validateCustomer(name, nationalId, email);
    Customer customer = new Customer(idCounter++, name, nationalId, email);
    customers.add(customer);
}
```

### Validation
```java
if (nationalId == null || !nationalId.matches("\\d{10}")) {
    throw new InvalidCustomerException("National ID must be exactly 10 digits.");
}
```

### Exception Handling
```java
try {
    customerService.addCustomer(name, nationalId, email);
} catch (InvalidCustomerException e) {
    System.out.println("Validation error: " + e.getMessage());
}
```

---

## Run

```
Run main.java
```

---

## Author

Mohaddeseh (Soudeh) Akmali  
