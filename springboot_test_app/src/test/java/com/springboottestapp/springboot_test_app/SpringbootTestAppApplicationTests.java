package com.springboottestapp.springboot_test_app;

import com.springboottestapp.springboot_test_app.exceptions.InsufficientMoneyException;
import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.Bank;
import com.springboottestapp.springboot_test_app.repositories.AccountRepository;
import com.springboottestapp.springboot_test_app.repositories.BankRepository;
import com.springboottestapp.springboot_test_app.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.springboottestapp.springboot_test_app.data.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestAppApplicationTests {

    @MockBean
    AccountRepository accountRepository;
    @MockBean
    BankRepository bankRepository;
    @Autowired
    AccountService accountService;

    /***
     Implemantation with mockito
     @Mock AccountRepository accountRepository;
     @Mock BankRepository bankRepository;
     @InjectMocks AccountServiceImpl accountService;*/

    @BeforeEach
    void setUp() {
//        accountRepository = mock(AccountRepository.class);
//        bankRepository = mock(BankRepository.class);
//        accountService = new AccountServiceImpl(accountRepository, bankRepository);
    }

    @Test
    void contextLoads() {
        when(accountRepository.findById(1l)).thenReturn(createAccount001());
        when(accountRepository.findById(2l)).thenReturn(createAccount002());
        when(bankRepository.findById(1l)).thenReturn(createBank());

        BigDecimal originMoney = accountService.checkMoney(1L);
        BigDecimal destinyMoney = accountService.checkMoney(2L);
        assertEquals("1000", originMoney.toPlainString());
        assertEquals("2000", destinyMoney.toPlainString());

        accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);

        originMoney = accountService.checkMoney(1L);
        destinyMoney = accountService.checkMoney(2L);

        assertEquals("900", originMoney.toPlainString());
        assertEquals("2100", destinyMoney.toPlainString());

        int total = accountService.checkTotalTransaction(1L);

        assertEquals(1, total);
        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository, times(2)).save(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());
        verify(accountRepository, never()).findAll();


    }

    @Test
    void contextLoads2() {
        when(accountRepository.findById(1l)).thenReturn(createAccount001());
        when(accountRepository.findById(2l)).thenReturn(createAccount002());
        when(bankRepository.findById(1l)).thenReturn(createBank());

        BigDecimal originMoney = accountService.checkMoney(1L);
        BigDecimal destinyMoney = accountService.checkMoney(2L);
        assertEquals("1000", originMoney.toPlainString());
        assertEquals("2000", destinyMoney.toPlainString());

        assertThrows(InsufficientMoneyException.class, () -> {
            accountService.transfer(1L, 2L, new BigDecimal("1200"), 1L);
        });

        originMoney = accountService.checkMoney(1L);
        destinyMoney = accountService.checkMoney(2L);

        assertEquals("1000", originMoney.toPlainString());
        assertEquals("2000", destinyMoney.toPlainString());

        int total = accountService.checkTotalTransaction(1L);

        assertEquals(0, total);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).save(any(Bank.class));

        verify(accountRepository, times(5)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void contextLoads3() {
        when(accountRepository.findById(1L)).thenReturn(createAccount001());

        Account account1 = accountService.findById(1l);
        Account account2 = accountService.findById(1l);

        assertSame(account1, account2);
        assertTrue(account1 == account2);

        assertEquals("Adrian", account1.getName());
        assertEquals("Adrian", account2.getName());

        verify(accountRepository, times(2)).findById(anyLong());
    }

    @Test
    void testFindAll() {
        //Given
        List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(), createAccount002().orElseThrow());
        when(accountRepository.findAll()).thenReturn(accounts);

        //when
        List<Account> accountsList = accountService.findAll();

        //then
        assertFalse(accountsList.isEmpty());
        assertEquals(2, accountsList.size());
        assertTrue(accountsList.contains(createAccount002().orElseThrow()));
    }

    @Test
    void testSave() {
        Account account = new Account(null, "Adrian", new BigDecimal("3000"));
        when(accountRepository.save(any())).then(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(3L);
            return a;
        });

        // when
        Account account1 = accountService.save(account);

        //then
        assertEquals("Adrian", account1.getName());
        assertEquals(3, account1.getId());
        assertEquals("3000", account.getMoney().toPlainString());

        verify(accountRepository).save(any());
    }

}
