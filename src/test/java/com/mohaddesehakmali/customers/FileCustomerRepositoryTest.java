package com.mohaddesehakmali.customers;

import com.mohaddesehakmali.customers.exception.CustomerStorageException;
import com.mohaddesehakmali.customers.repository.FileCustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FileCustomerRepositoryTest {
    @TempDir Path temp;

    @Test void missingFileStartsEmpty() {
        var repository = new FileCustomerRepository(temp.resolve("customers.db"));
        assertTrue(repository.findAll().isEmpty());
        assertTrue(repository.findById(55).isEmpty());
        assertFalse(repository.deleteById(55));
    }

    @Test void corruptedFileFailsClearlyRatherThanLosingData() throws Exception {
        Path file = temp.resolve("customers.db");
        Files.writeString(file, "garbled file\n");
        assertThrows(CustomerStorageException.class, () -> new FileCustomerRepository(file));
        assertEquals("garbled file\n", Files.readString(file));
    }

    @Test void unicodeAndSpecialCharactersRoundTrip() {
        Path file = temp.resolve("customers.db");
        var repository = new FileCustomerRepository(file);
        repository.create("Mina, \tمینا", "1234567890", "mina@example.com");
        assertEquals("Mina, \tمینا", new FileCustomerRepository(file).findById(1).orElseThrow().name());
    }
}
