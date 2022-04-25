package com.springboottestapp.springboot_test_app.repositories;

import com.springboottestapp.springboot_test_app.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    /*List<Bank> findAll();
    Bank findById(Long id);
    void update(Bank bank);*/
}
