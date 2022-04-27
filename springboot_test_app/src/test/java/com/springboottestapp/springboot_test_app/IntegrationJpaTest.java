package com.springboottestapp.springboot_test_app;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
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

    @Test
    void testSave() {
        //given
        Account accountP = new Account(null, "Oscar", new BigDecimal("3000"));


        //when
        Account accountS = accountRepository.save(accountP);
        //Account accountR = accountRepository.findByName("Oscar").orElseThrow();
        //Account accountR = accountRepository.findById(accountS.getId()).orElseThrow();

        //then
        assertEquals("Oscar", accountS.getName());
        assertEquals("3000", accountS.getMoney().toPlainString());
    }

    @Test
    void testUpdate() {
        //Given
        Account accountP = new Account(null, "Erick", new BigDecimal("3000"));
        //when
        Account accountR = accountRepository.save(accountP);

        //then
        assertEquals("Erick", accountR.getName());
        assertEquals("3000", accountR.getMoney().toPlainString());

        //when
        accountR.setMoney(new BigDecimal("3800"));
        Account accountUpdated = accountRepository.save(accountR);

        //then
        assertEquals("Erick", accountUpdated.getName());
        assertEquals("3800", accountUpdated.getMoney().toPlainString());
    }

    @Test
    void testDelete() {
        Account account = accountRepository.findById(2L).orElseThrow();
        assertEquals("Eduardo", account.getName());

        accountRepository.delete(account);

        assertThrows(NoSuchElementException.class, ()-> {
           accountRepository.findById(2L).orElseThrow();
        });

        assertEquals(1, accountRepository.findAll().size());
    }
}
