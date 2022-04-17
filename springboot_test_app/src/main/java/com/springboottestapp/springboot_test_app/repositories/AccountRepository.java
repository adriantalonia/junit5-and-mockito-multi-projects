package com.springboottestapp.springboot_test_app.repositories;

import com.springboottestapp.springboot_test_app.models.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository {
    List<Account> findALl();
    Account findById(Long id);
    void update(Account account);
}
