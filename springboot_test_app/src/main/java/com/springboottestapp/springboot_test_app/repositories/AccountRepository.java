package com.springboottestapp.springboot_test_app.repositories;

import com.springboottestapp.springboot_test_app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.name=?1")
    Optional<Account> findByName(String name);
    //List<Account> findALl();
    //Account findById(Long id);
    //void update(Account account);
}
