package com.springboottestapp.springboot_test_app.repositories;

import com.springboottestapp.springboot_test_app.models.Bank;

import java.util.List;

public interface BankRepository {
    List<Bank> findAll();
    Bank findById();
    void update(Bank bank);
}
