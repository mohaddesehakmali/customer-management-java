package com.mohaddesehakmali.customers;

import com.mohaddesehakmali.customers.exception.*;
import com.mohaddesehakmali.customers.repository.FileCustomerRepository;
import com.mohaddesehakmali.customers.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {
    @TempDir Path temp;

    private CustomerService service() {
        return new CustomerService(new FileCustomerRepository(temp.resolve("customers.db")));
    }

    @Test void createsAndListsCustomers() {
        var service = service();
        var created = service.addCustomer(" Alice ", "1234567890", "alice@example.com");
        assertEquals(1, created.id());
        assertEquals("Alice", created.name());
        assertEquals(1, service.getAllCustomers().size());
        assertEquals(created, service.getCustomerById(1));
    }

    @Test void rejectsInvalidInput() {
        var service = service();
        assertThrows(InvalidCustomerException.class, () -> service.addCustomer(" ", "1234567890", "valid@example.com"));
        assertThrows(InvalidCustomerException.class, () -> service.addCustomer("Alice", "123", "valid@example.com"));
        assertThrows(InvalidCustomerException.class, () -> service.addCustomer("Alice", "1234567890", "invalid"));
        assertTrue(service.getAllCustomers().isEmpty());
    }

    @Test void rejectsDuplicateIdentifiersAndEmailsIgnoringCase() {
        var service = service();
        service.addCustomer("Alice", "1234567890", "Alice@example.com");
        assertThrows(DuplicateCustomerException.class, () ->
                service.addCustomer("Bob", "1234567890", "bob@example.com"));
        assertThrows(DuplicateCustomerException.class, () ->
                service.addCustomer("Bob", "9999999999", "alice@EXAMPLE.com"));
    }

    @Test void updatesAndDeletes() {
        var service = service();
        var a = service.addCustomer("Alice", "1234567890", "alice@example.com");
        var b = service.addCustomer("Bob", "1111111111", "bob@example.com");
        var updated = service.updateCustomer(a.id(), "Alice Smith", a.nationalId(), "alice.smith@example.com");
        assertEquals("Alice Smith", updated.name());
        assertEquals(2, service.getAllCustomers().size());
        assertThrows(DuplicateCustomerException.class, () ->
                service.updateCustomer(b.id(), "Bob", a.nationalId(), "bob@example.com"));
        service.deleteCustomer(a.id());
        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(a.id()));
        assertEquals(1, service.getAllCustomers().size());
    }

    @Test void rejectsMissingCustomers() {
        var service = service();
        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(99));
        assertThrows(CustomerNotFoundException.class, () ->
                service.updateCustomer(99, "Alice", "1234567890", "alice@example.com"));
        assertThrows(CustomerNotFoundException.class, () -> service.deleteCustomer(99));
    }

    @Test void persistsAcrossRestartsWithoutReusingDeletedIds() {
        var service = service();
        var first = service.addCustomer("Alice", "1234567890", "alice@example.com");
        service.addCustomer("Bob", "1111111111", "bob@example.com");
        service.deleteCustomer(first.id());
        var restarted = service();
        assertEquals(1, restarted.getAllCustomers().size());
        assertEquals("Bob", restarted.getCustomerById(2).name());
        assertEquals(3, restarted.addCustomer("Charlie", "2222222222", "charlie@example.com").id());
    }
}
