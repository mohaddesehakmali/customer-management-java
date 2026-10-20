# Customer Management — Java Console App

A Java 17 portfolio project demonstrating **OOP, layered design, CRUD operations, input validation, file persistence, exception handling, and JUnit 5 testing**. There is no Spring Boot dependency; the goal is to demonstrate core Java concepts clearly.

## Highlights

- Create, list, find, edit and delete customers.
- Validate name, email, and a 10-digit customer national identifier.
- Reject duplicate national identifiers and duplicate emails (case-insensitive).
- Separate presentation (console), business rules (service), and storage (repository).
- Persist records to disk; IDs continue increasing even after app restarts or deletions.
- Write updates atomically via a temporary file and file replacement, so failed writes do not partially overwrite data.
- Give clear error messages for invalid input, missing customers and storage errors.
- Keep generated files, local IDE settings and customer records out of Git.
- Use automated unit and persistence integration tests with GitHub Actions.

## Requirements

Java **17+** and Maven **3.9+**.

## Run

```bash
mvn clean verify
mvn exec:java -Dexec.mainClass=com.mohaddesehakmali.customers.CustomerManagementApplication
```

If the Maven exec plugin is not available in your environment, run directly:

```bash
mvn -q -DskipTests package
java -cp target/classes com.mohaddesehakmali.customers.CustomerManagementApplication
```

Windows PowerShell:

```powershell
mvn clean verify
java -cp target/classes com.mohaddesehakmali.customers.CustomerManagementApplication
```

By default, records are stored in `data/customers.db` relative to the working directory. To choose a different location:

```bash
java -Dcustomer.data.file=/path/to/customers.db -cp target/classes com.mohaddesehakmali.customers.CustomerManagementApplication
```

This is an **educational local file store**, not a multi-user database. Only one application process should write to the same file at once. Records are not encrypted; avoid using real sensitive personal information.

## Architecture

```src/main/java/com/mohaddesehakmali/customers/
  CustomerManagementApplication.java   # CLI
  model/Customer.java                   # Immutable customer record
  service/CustomerService.java          # Business rules
  validation/CustomerValidator.java     # Input validation
  repository/CustomerRepository.java    # Persistence contract
  repository/FileCustomerRepository.java
  exception/                             # Domain / persistence errors
```

The repository stores a version header, a persisted next-ID counter and base64-encoded UTF-8 fields in a tab-separated file. Base64 is for unambiguous storage, **not security/encryption**.

## Tests

`mvn verify` runs JUnit 5 tests covering CRUD, validation, duplicates, missing records, update behavior, restart persistence, deletion, ID monotonicity and handling corrupted files.

## What to add next

A REST API in Spring Boot and a database-backed repository (for multi-user scenarios) would be suitable follow-on exercises.
