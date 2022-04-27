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
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account details(@PathVariable Long id) {
        return accountService.findById(id);
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


}
