package com.springboottestapp.springboot_test_app.controllers;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.TransactionDTO;
import com.springboottestapp.springboot_test_app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> details(@PathVariable Long id) {

        Account account = null;
        try {
            account = accountService.findById(id);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDTO dto) {
        accountService.transfer(dto.getOriginAccount(), dto.getDestinyAccount(), dto.getAmount(), dto.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Successfully transaction");
        response.put("transaction", dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> list() {
        return accountService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account save(@RequestBody Account account) {
        return accountService.save(account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountService.deleteById(id);
    }

}
