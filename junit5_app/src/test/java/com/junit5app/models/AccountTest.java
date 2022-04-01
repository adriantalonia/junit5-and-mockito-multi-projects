package com.junit5app.models;

import com.junit5app.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //with this you can remove statuc
class AccountTest {

    Account account;

    @BeforeEach
    void initMethod() {
        this.account = new Account("Adrian", new BigDecimal("1000.12345"));
        System.out.println("Init method");
    }

    @AfterEach
    void finalMethod() {
        System.out.println("Ending test method");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Initialize test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Complete test");
    }


    @Test
    @DisplayName("test user name")
        //name a method
    void testUser() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        String expected = "Adrian";
        String real = account.getUser();
        assertEquals(expected, real);
        assertTrue(real.equals(expected));
    }

    @Test
    @Disabled
        //disabled method but that show in the report
    void testMoney() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getMoney().doubleValue());
        assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testReferAccount() {
        Account account = new Account("Adrian", new BigDecimal("1000.4789"));
        Account account2 = new Account("Adrian", new BigDecimal("1000.4789"));
        //assertNotEquals(account2, account); //compare reference
        assertEquals(account2, account); // two object different location in memory
    }

    @Test
    void testAccountDebit() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getMoney());
        assertEquals(900, account.getMoney().intValue());
        assertEquals("900.12345", account.getMoney().toPlainString());
    }

    @Test
    void testAccountCredit() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal(100));
        assertNotNull(account.getMoney());
        assertEquals(1100, account.getMoney().intValue());
        assertEquals("1100.12345", account.getMoney().toPlainString());
    }

    @Test
    void insufficientMoneyException() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(InsufficientMoneyException.class, () -> { //lambda expression
            account.debit(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String expected = "Insufficient Money";
        assertEquals(expected, actual);
    }

    @Test
    void testTransferMoney() {
        Account account = new Account("Jon", new BigDecimal("2500"));
        Account account2 = new Account("Adrian", new BigDecimal("1500.8989"));
        Bank bank = new Bank();
        bank.transfer(account2, account, new BigDecimal(500));
        assertEquals("1000.8989", account2.getMoney().toPlainString());
        assertEquals("3000", account.getMoney().toPlainString());
    }

    @Test
    void testRelationBankAccounts() {
        Account account = new Account("Jon", new BigDecimal("2500"));
        Account account2 = new Account("Adrian", new BigDecimal("1500.8989"));

        Bank bank = new Bank();
        bank.setName("BBVA");
        bank.addAccount(account);
        bank.addAccount(account2);

        assertAll(
                () -> assertEquals(2, bank.getAccounts().size(), "no equals banks"),
                () -> assertEquals("BBVA", account.getBank().getName(), () -> "no equals banks"), //string will create when is not equal
                () -> assertEquals("Adrian", bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().get().getUser()),
                () -> {
                    assertTrue(bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().isPresent());
                },
                () -> {
                    assertTrue(bank.getAccounts().stream().anyMatch(c -> c.getUser().equals("Adrian")));
                });

        /*assertEquals(2, bank.getAccounts().size());
        assertEquals("BBVA", account.getBank().getName());
        assertEquals("Adrian", bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().get().getUser());
        assertTrue(bank.getAccounts().stream().filter(c -> c.getUser().equals("Adrian")).findFirst().isPresent());
        assertTrue(bank.getAccounts().stream().anyMatch(c -> c.getUser().equals("Adrian")));*/
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
        // execute only windows
    void testOnlyWindows() {
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testOnlyLinux() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testOnlyJava8() {
    }

    @Test
    void testSystemProperties() {
        Properties p = System.getProperties();
        p.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "11.0.14.1")
    void testSystemProperty() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testOnly64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "USERNAME", matches = "adrian")
    void testUserName() {
    }

    @Test
    void getEnv() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*java-11.*")
    void testJavaHome() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
    void testEnv() {
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
    void testEnvProdDisabled() {
    }

}