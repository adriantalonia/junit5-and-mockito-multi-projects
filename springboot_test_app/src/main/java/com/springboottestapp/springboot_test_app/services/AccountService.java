package com.springboottestapp.springboot_test_app.services;

import com.springboottestapp.springboot_test_app.models.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account findById(Long id);
    int checkTotalTransaction(Long bankId);
    BigDecimal checkMoney(Long accountId);
    void transfer(Long originAccount, Long destinyAccount);
}
