package com.mohaddesehakmali.customers.repository;

import com.mohaddesehakmali.customers.exception.CustomerStorageException;
import com.mohaddesehakmali.customers.model.Customer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;

public final class FileCustomerRepository implements CustomerRepository {
    private static final String HEADER = "CUSTOMERS_V1";
    private final Path file;
    private final Map<Integer, Customer> customers = new LinkedHashMap<>();
    private int nextId = 1;

    public FileCustomerRepository(Path file) {
        this.file = Objects.requireNonNull(file).toAbsolutePath();
        load();
    }

    @Override public synchronized List<Customer> findAll() {
        return List.copyOf(customers.values());
    }

    @Override public synchronized Optional<Customer> findById(int id) {
        return Optional.ofNullable(customers.get(id));
    }

    @Override public synchronized Customer create(String name, String nationalId, String email) {
        if (nextId == Integer.MAX_VALUE) throw new IllegalStateException("Customer ID limit reached");
        Customer customer = new Customer(nextId, name, nationalId, email);
        Map<Integer, Customer> updated = new LinkedHashMap<>(customers);
        updated.put(customer.id(), customer);
        persist(updated, nextId + 1);
        customers.put(customer.id(), customer);
        nextId++;
        return customer;
    }

    @Override public synchronized Customer update(Customer customer) {
        Objects.requireNonNull(customer);
        if (!customers.containsKey(customer.id())) throw new IllegalArgumentException("Unknown customer ID");
        Map<Integer, Customer> updated = new LinkedHashMap<>(customers);
        updated.put(customer.id(), customer);
        persist(updated, nextId);
        customers.put(customer.id(), customer);
        return customer;
    }

    @Override public synchronized boolean deleteById(int id) {
        if (!customers.containsKey(id)) return false;
        Map<Integer, Customer> updated = new LinkedHashMap<>(customers);
        updated.remove(id);
        persist(updated, nextId);
        customers.remove(id);
        return true;
    }

    private void load() {
        if (!Files.exists(file)) return;
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            if (lines.size() < 2 || !HEADER.equals(lines.get(0))) {
                throw new IllegalArgumentException("Unknown file header");
            }
            int storedNextId = Integer.parseInt(lines.get(1));
            if (storedNextId < 1) throw new IllegalArgumentException("Invalid next ID");
            Map<Integer, Customer> loaded = new LinkedHashMap<>();
            int maxId = 0;
            for (int i = 2; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\\t", -1);
                if (parts.length != 4) throw new IllegalArgumentException("Malformed customer row");
                int id = Integer.parseInt(parts[0]);
                if (id <= 0 || loaded.containsKey(id)) throw new IllegalArgumentException("Invalid or duplicate ID");
                Customer customer = new Customer(id, decode(parts[1]), decode(parts[2]), decode(parts[3]));
                loaded.put(id, customer);
                maxId = Math.max(maxId, id);
            }
            if (storedNextId <= maxId) throw new IllegalArgumentException("Invalid next ID counter");
            customers.putAll(loaded);
            nextId = storedNextId;
        } catch (IOException | IllegalArgumentException e) {
            throw new CustomerStorageException("Cannot load customer file: " + file, e);
        }
    }

    private void persist(Map<Integer, Customer> items, int newNextId) {
        Path temporary = null;
        try {
            Path parent = file.getParent();
            Files.createDirectories(parent);
            temporary = Files.createTempFile(parent, ".customers-", ".tmp");
            List<String> lines = new ArrayList<>();
            lines.add(HEADER);
            lines.add(Integer.toString(newNextId));
            for (Customer c : items.values()) {
                lines.add(c.id() + "\t" + encode(c.name()) + "\t" +
                        encode(c.nationalId()) + "\t" + encode(c.email()));
            }
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new CustomerStorageException("Cannot save customers to " + file, e);
        } finally {
            if (temporary != null) {
                try { Files.deleteIfExists(temporary); } catch (IOException ignored) { }
            }
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
