package com.springboottestapp.springboot_test_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboottestapp.springboot_test_app.models.TransactionDTO;
import com.springboottestapp.springboot_test_app.services.AccountService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.springboottestapp.springboot_test_app.data.Data.*;
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
        // When
        mvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tran)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Successfully transaction"))
                .andExpect(jsonPath("$.transaction.originAccount").value(tran.getOriginAccount()));
    }
}