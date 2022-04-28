package com.springboottestapp.springboot_test_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.TransactionDTO;
import com.springboottestapp.springboot_test_app.services.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboottestapp.springboot_test_app.data.Data.createAccount001;
import static com.springboottestapp.springboot_test_app.data.Data.createAccount002;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void details() throws Exception {
        // given
        when(accountService.findById(1L)).thenReturn(createAccount001().orElseThrow());

        // when
        mvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Adrian"))
                .andExpect(jsonPath("$.money").value("1000"));

        verify(accountService).findById(1l);
    }

    @Test
    void transfer() throws Exception, JsonProcessingException {
        // Given
        TransactionDTO tran = new TransactionDTO();
        tran.setOriginAccount(1L);
        tran.setDestinyAccount(2L);
        tran.setAmount(new BigDecimal("100"));
        tran.setBankId(1L);

        System.out.println(objectMapper.writeValueAsString(tran));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Successfully transaction");
        response.put("transaction", tran);

        System.out.println(objectMapper.writeValueAsString(response));

        // When
        mvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tran)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Successfully transaction"))
                .andExpect(jsonPath("$.transaction.originAccount").value(tran.getOriginAccount()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testList() throws Exception, JsonProcessingException {
        // Given
        List<Account> accounts = Arrays.asList(createAccount001().orElseThrow(), createAccount002().orElseThrow());
        when(accountService.findAll()).thenReturn(accounts);

        // When
        mvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Adrian"))
                .andExpect(jsonPath("$[1].name").value("Eduardo"))
                .andExpect(jsonPath("$[0].money").value("1000"))
                .andExpect(jsonPath("$[1].money").value("2000"))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));

    }

    @Test
    void testSave() throws Exception, JsonProcessingException {
        // Given
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));
        when(accountService.save(any())).then(invocation -> {
            Account a = invocation.getArgument(0);
            a.setId(3L);
            return a;
        });

        // when
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.name", Matchers.is("Pepe")))
                .andExpect(jsonPath("$.money", Matchers.is(3000)));
        verify(accountService).save(any());
    }
}