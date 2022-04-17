package com.springboottestapp.springboot_test_app.services;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.Bank;
import com.springboottestapp.springboot_test_app.repositories.AccountRepository;
import com.springboottestapp.springboot_test_app.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public int checkTotalTransaction(Long bankId) {
        Bank bank = bankRepository.findById(bankId);
        return bank.getTransactions();
    }

    @Override
    public BigDecimal checkMoney(Long accountId) {
        Account account = accountRepository.findById(accountId);
        return account.getMoney();
    }

    @Override
    public void transfer(Long originAccount, Long destinyAccount, BigDecimal amount, Long bankId) {

        Account oriAccount = accountRepository.findById(originAccount);
        oriAccount.debit(amount);
        accountRepository.update(oriAccount);

        Account desAccount = accountRepository.findById(destinyAccount);
        desAccount.credit(amount);
        accountRepository.update(desAccount);

        Bank bank = bankRepository.findById(1L);
        int totalTransfer = bank.getTransactions();
        bank.setTransactions(++totalTransfer);
        bankRepository.update(bank);
    }
}
