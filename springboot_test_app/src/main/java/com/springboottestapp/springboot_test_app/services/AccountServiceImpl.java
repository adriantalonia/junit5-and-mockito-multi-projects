package com.springboottestapp.springboot_test_app.services;

import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.Bank;
import com.springboottestapp.springboot_test_app.repositories.AccountRepository;
import com.springboottestapp.springboot_test_app.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransaction(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTransactions();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkMoney(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getMoney();
    }

    @Override
    @Transactional
    public void transfer(Long originAccount, Long destinyAccount, BigDecimal amount, Long bankId) {

        Account oriAccount = accountRepository.findById(originAccount).orElseThrow();
        oriAccount.debit(amount);
        accountRepository.save(oriAccount);

        Account desAccount = accountRepository.findById(destinyAccount).orElseThrow();
        desAccount.credit(amount);
        accountRepository.save(desAccount);

        Bank bank = bankRepository.findById(1L).orElseThrow();
        int totalTransfer = bank.getTransactions();
        bank.setTransactions(++totalTransfer);
        bankRepository.save(bank);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

}
