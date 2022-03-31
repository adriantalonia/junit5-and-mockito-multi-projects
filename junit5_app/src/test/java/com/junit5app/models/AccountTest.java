package com.junit5app.models;

import com.junit5app.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testUser() {
        Account account = new Account("Adrian", new BigDecimal("1000.4789"));
        String expected = "Adrian";
        String real = account.getUser();
        assertEquals(expected, real);
        assertTrue(real.equals(expected));
    }

    @Test
    void testMoney() {
        Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getMoney().doubleValue());
        assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testReferAccount() {
        Account account = new Account("Adrian", new BigDecimal("1000.4789"));
        Account account2 = new Account("Adrian", new BigDecimal("1000.4789"));
        //assertNotEquals(account2, account); //compare reference
        assertEquals(account2, account); // two object different location in memory
    }

    @Test
    void testAccountDebit() {
        Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getMoney());
        assertEquals(900, account.getMoney().intValue());
        assertEquals("900.12345", account.getMoney().toPlainString());
    }

    @Test
    void testAccountCredit() {
        Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal(100));
        assertNotNull(account.getMoney());
        assertEquals(1100, account.getMoney().intValue());
        assertEquals("1100.12345", account.getMoney().toPlainString());
    }

    @Test
    void insufficientMoneyException() {
        Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(InsufficientMoneyException.class, () -> { //lambda expression
            account.debit(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String expected = "Insufficient Money";
        assertEquals(expected, actual);
    }

    @Test
    void testTransferMoney() {
        Account account = new Account("Jon", new BigDecimal("2500"));
        Account account2 = new Account("Adrian", new BigDecimal("1500.8989"));
        Bank bank = new Bank();
        bank.transfer(account2, account, new BigDecimal(500));
        assertEquals("1000.8989", account2.getMoney().toPlainString());
        assertEquals("3000", account.getMoney().toPlainString());
    }

    @Test
    void testRelationBankAccounts() {
        Account account = new Account("Jon", new BigDecimal("2500"));
        Account account2 = new Account("Adrian", new BigDecimal("1500.8989"));

        Bank bank = new Bank();
        bank.setName("BBVA");
        bank.addAccount(account);
        bank.addAccount(account2);

        assertAll(
                () -> assertEquals(2, bank.getAccounts().size(), "no equals banks"),
                () -> assertEquals("BBVA", account.getBank().getName(), () -> "no equals banks"), //string will create when is not equal
                () -> assertEquals("Adrian", bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().get().getUser()),
                () -> {
                    assertTrue(bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().isPresent());
                },
                () -> {
                    assertTrue(bank.getAccounts().stream().anyMatch(c -> c.getUser().equals("Adrian")));
                });

        /*assertEquals(2, bank.getAccounts().size());
        assertEquals("BBVA", account.getBank().getName());
        assertEquals("Adrian", bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().get().getUser());
        assertTrue(bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().isPresent());
        assertTrue(bank.getAccounts().stream().anyMatch(c -> c.getUser().equals("Adrian")));*/
    }
}