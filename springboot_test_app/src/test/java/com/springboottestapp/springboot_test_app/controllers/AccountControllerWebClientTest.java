package com.springboottestapp.springboot_test_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboottestapp.springboot_test_app.models.Account;
import com.springboottestapp.springboot_test_app.models.TransactionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration_wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransfer() throws Exception, JsonProcessingException {
        //given
        TransactionDTO dto = new TransactionDTO();
        dto.setOriginAccount(1L);
        dto.setDestinyAccount(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Successfully transaction");
        response.put("transaction", dto);

        //when
        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(res -> {
                    JsonNode json = null;
                    try {
                        json = objectMapper.readTree(res.getResponseBody());
                        assertEquals("Successfully transaction", json.path("message").asText());
                        assertEquals(1, json.path("transaction").path("originAccount").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Successfully transaction"))
                .jsonPath("$.message").value(valor -> {
                    assertEquals("Successfully transaction", valor);
                })
                .jsonPath("$.message").isEqualTo("Successfully transaction")
                .jsonPath("$.transaction.originAccount").isEqualTo(dto.getOriginAccount())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void testDetail() throws Exception, JsonProcessingException {

        Account account = new Account(1L, "Adrian", new BigDecimal("900"));

        client.get().uri("/api/accounts/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Adrian")
                .jsonPath("$.money").isEqualTo(900)
                .json(objectMapper.writeValueAsString(account));
    }

    @Test
    @Order(3)
    void testDetail2() {
        client.get().uri("/api/accounts/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(res -> {
                    Account account = res.getResponseBody();
                    assertEquals("Eduardo", account.getName());
                    assertEquals("2100.00", account.getMoney().toPlainString());
                });

    }

    @Test
    @Order(4)
    void testList() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Adrian")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].money").isEqualTo(900)
                .jsonPath("$[1].name").isEqualTo("Eduardo")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].money").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    void testList2() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(res -> {
                    List<Account> accounts = res.getResponseBody();
                    assertEquals(2, accounts.size());
                    assertEquals(1L, accounts.get(0).getId());
                    assertEquals("Adrian", accounts.get(0).getName());
                    assertEquals("900.0", accounts.get(0).getMoney().toPlainString());
                    assertEquals(2L, accounts.get(1).getId());
                    assertEquals("Eduardo", accounts.get(1).getName());
                    assertEquals("2100.0", accounts.get(1).getMoney().toPlainString());
                })
                .hasSize(2)
                .value(hasSize(2));
    }

    @Test
    @Order(6)
    void testSave() throws Exception {

        Account account = new Account(null, "Oscar", new BigDecimal("3000"));

        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.name").isEqualTo("Oscar")
                .jsonPath("$.money").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void testSave2() throws Exception {

        Account account = new Account(null, "Erick", new BigDecimal("3500"));

        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(res -> {
                    Account ac = res.getResponseBody();
                    assertNotNull(ac);
                    assertEquals(4L, ac.getId());
                    assertEquals("Erick", ac.getName());
                    assertEquals("3500", ac.getMoney().toPlainString());

                });
    }

    @Test
    @Order(8)
    void testDelete() {

        client.get().uri("/api/accounts").exchange()
                        .expectStatus().isOk()
                        .expectBodyList(Account.class)
                                .hasSize(4);

        client.delete().uri("/api/accounts/3").exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectBodyList(Account.class)
                .hasSize(3);

        client.get().uri("/api/accounts/3").exchange()
                //.expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}