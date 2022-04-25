package com.springboottestapp.springboot_test_app.data;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {

    /*public static final Account ACCOUNT_001 = new Account(1L, "Adrian", new BigDecimal("1000"));
    public static final Account ACCOUNT_002 = new Account(2L, "Eduardo", new BigDecimal("2000"));
    public static final Bank BANK = new Bank(1l, "BBVA", 0);*/

    public static Optional<Account> createAccount001() {
        return Optional.of(new Account(1L, "Adrian", new BigDecimal("1000")));
    }

    public static Optional<Account> createAccount002() {
        return Optional.of(new Account(2L, "Eduardo", new BigDecimal("2000")));
    }

    public static Optional<Bank> createBank() {
        return Optional.of(new Bank(1l, "BBVA", 0));
    }

}
