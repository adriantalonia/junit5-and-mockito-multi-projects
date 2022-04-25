package com.springboottestapp.springboot_test_app;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    AccountRepository accountRepository;

    @Test
    void testFindById(){
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Adrian", account.orElseThrow().getName());
    }

    @Test
    void testFindByName(){
        Optional<Account> account = accountRepository.findByName("Adrian");
        assertTrue(account.isPresent());
        assertEquals("Adrian", account.orElseThrow().getName());
        assertEquals("1000.00", account.orElseThrow().getMoney().toPlainString());
    }

    @Test
    void testFindByNameThrowException(){
        Optional<Account> account = accountRepository.findByName("Rob");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void testFindAll(){
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }
}
