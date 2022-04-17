package com.springboottestapp.springboot_test_app.repositories;

import com.springboottestapp.springboot_test_app.models.Bank;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository {
    List<Bank> findAll();
    Bank findById(Long id);
    void update(Bank bank);
}
