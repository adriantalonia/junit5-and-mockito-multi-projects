package com.springboottestapp.springboot_test_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.TransactionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration_rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransfer() throws JsonProcessingException {
        //given
        TransactionDTO dto = new TransactionDTO();
        dto.setOriginAccount(1L);
        dto.setDestinyAccount(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1L);

        ResponseEntity<String> response =
                client.postForEntity(createURI("/api/accounts/transfer"), dto, String.class);

        String json = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json != null);
        assertTrue(json.contains("Successfully transaction"));
        //assertTrue(json.contains("{\"originAccount\":1,\"destinyAccount\":2,\"amount\":100,\"bankId:\":1}"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Successfully transaction", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaction").path("amount").asText());
        assertEquals(1L, jsonNode.path("transaction").path("originAccount").asLong());

        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "OK");
        response2.put("message", "Successfully transaction");
        response2.put("transaction", dto);

        assertEquals(objectMapper.writeValueAsString(response2), json);

    }

    private String createURI(String uri) {
        return "http://localhost:"+port+uri;
    }

    @Test
    @Order(2)
    void testDetail() {
        ResponseEntity<Account> response = client.getForEntity(createURI("/api/accounts/1"), Account.class);
        Account account = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals("Adrian", account.getName());
        assertEquals("900.00", account.getMoney().toPlainString());
        assertEquals(new Account(1l, "Adrian", new BigDecimal("900.00")), account);
    }

    @Test
    @Order(3)
    void testList() throws JsonProcessingException {
        ResponseEntity<Account[]> response = client.getForEntity(createURI("/api/accounts"), Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(1L, accounts.get(0).getId());
        assertEquals("Adrian", accounts.get(0).getName());
        assertEquals("900.00", accounts.get(0).getMoney().toPlainString());

        assertEquals(2L, accounts.get(1).getId());
        assertEquals("Eduardo", accounts.get(1).getName());
        assertEquals("2100.00", accounts.get(1).getMoney().toPlainString());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(accounts));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Adrian", json.get(0).path("name").asText());
        assertEquals("900.0", json.get(0).path("money").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Eduardo", json.get(1).path("name").asText());
        assertEquals("2100.0", json.get(1).path("money").asText());

    }

    @Test
    @Order(4)
    void testSave() {
        Account account = new Account(null, "Oscar", new BigDecimal("3800"));

        ResponseEntity<Account> response = client.postForEntity(createURI("/api/accounts"), account, Account.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Account accountCreated = response.getBody();
        assertEquals(3L, accountCreated.getId());
        assertEquals("Oscar", accountCreated.getName());
        assertEquals("3800", accountCreated.getMoney().toPlainString());

    }

    @Test
    @Order(5)
    void testDelete() {
        ResponseEntity<Account[]> response = client.getForEntity(createURI("/api/accounts"), Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());
        assertEquals(3, accounts.size());

        //client.delete(createURI("/api/accounts/3"));
        Map<String, Long> pathVariable = new HashMap<>();
        pathVariable.put("id", 3L);
        ResponseEntity<Void> exchange = client.exchange(createURI("/api/accounts/{id}"), HttpMethod.DELETE, null, Void.class,
                pathVariable);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        ResponseEntity<Account[]> response2 = client.getForEntity(createURI("/api/accounts"), Account[].class);
        List<Account> accounts2 = Arrays.asList(response2.getBody());
        assertEquals(2, accounts2.size());

        ResponseEntity<Account> response3 = client.getForEntity(createURI("/api/accounts/3"), Account.class);
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
        assertFalse(response3.hasBody());
    }

}