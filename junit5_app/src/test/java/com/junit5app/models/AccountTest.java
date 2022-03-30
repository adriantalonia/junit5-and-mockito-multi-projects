package com.junit5app.models;

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
}