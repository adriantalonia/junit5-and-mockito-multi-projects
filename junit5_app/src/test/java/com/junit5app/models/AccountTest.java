package com.junit5app.models;

import com.junit5app.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //with this you can remove static
class AccountTest {

    Account account;
    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeEach
    void initMethod(TestInfo testInfo, TestReporter testReporter) {

        testReporter.publishEntry("executing: "+testInfo.getDisplayName() + " "+ testInfo.getTestMethod().orElse(null).getName() + " " +
                " with the tags "+ testInfo.getTags());

        this.testInfo = testInfo;
        this.testReporter = testReporter;

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
    @DisplayName("test user name") //name a method
    void testUser() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));

        String expected = "Adrian";
        String real = account.getUser();
        assertEquals(expected, real);
        assertTrue(real.equals(expected));
    }

    @Test
    @Disabled//disabled method but that show in the report
    void testMoney() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getMoney().doubleValue());
        assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
    }

    @DisplayName("Repeat Test")
    @RepeatedTest(value = 5, name = "{displayName} Iteration {currentRepetition} of {totalRepetitions}")
    void testReferAccount(RepetitionInfo info) {

        if (info.getCurrentRepetition() == 3) {
            System.out.println("Here is " + info.getCurrentRepetition());
        }

        Account account = new Account("Adrian", new BigDecimal("1000.4789"));
        Account account2 = new Account("Adrian", new BigDecimal("1000.4789"));
        //assertNotEquals(account2, account); //compare reference
        assertEquals(account2, account); // two object different location in memory
    }

    @Nested
    class ParameterizedTests {

        @ParameterizedTest(name = "number {index} executing with value {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "400", "500", "700", "1000.12345"})
            //@ValueSource(doubles = {100, 200, 300, 400, 500, 700, 1000})
        void testAccountDebitParameterized(String amount) {
            Account account = new Account("Adrian", new BigDecimal("1000.12345"));
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getMoney());
            assertTrue(account.getMoney().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with value {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
        void testAccountDebitParameterized2(String index, String amount) {
            Account account = new Account("Adrian", new BigDecimal("1000.12345"));
            System.out.println(index + " -> " + amount);
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getMoney());
            assertTrue(account.getMoney().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with value {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testAccountDebitParameterized3(String amount) {
            Account account = new Account("Adrian", new BigDecimal("1000.12345"));
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getMoney());
            assertTrue(account.getMoney().compareTo(BigDecimal.ZERO) > 0);
        }



        @ParameterizedTest(name = "number {index} executing with value {0} - {argumentsWithNames}")
        @CsvSource({"200,100", "250,200", "300,300", "510,500", "750,700", "1000.12345,1000.12345"})
        void testAccountDebitParameterized5(String money, String amount) {
            System.out.println(money + " -> " + amount);
            account.setMoney(new BigDecimal(money));
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getMoney());
            assertTrue(account.getMoney().compareTo(BigDecimal.ZERO) > 0);
        }
    }

    @ParameterizedTest(name = "number {index} executing with value {0} - {argumentsWithNames}")
    @MethodSource("listAmount")
    void testAccountDebitParameterized4(String amount) {
        Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(amount));
        assertNotNull(account.getMoney());
        assertTrue(account.getMoney().compareTo(BigDecimal.ZERO) > 0);
    }

    public static List<String> listAmount() {
        return Arrays.asList("100", "200", "300", "400", "500", "700", "1000.12345");
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

    @Nested
    @DisplayName("Testing Operative System")
    class OperativeSystemTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
            // execute only windows
        void testOnlyWindows() {
        }

        @Test
        @EnabledOnOs(OS.LINUX)
        void testOnlyLinux() {
        }
    }

    @Tag("param") // just execute a tap
    @Nested
    @DisplayName("Testing Java Version")
    class JavaVersionTest {
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
            if(testInfo.getTags().contains("param")) {
                System.out.println("do something with the tag ");
            }
        }
    }

    @Nested
    @DisplayName("Testing System Properties")
    class SystemPropertiesTest {
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


    @Test
    void testMoneyAssumptionDev() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(isDev); // dont execute asserts
        assertEquals(1000.12345, account.getMoney().doubleValue());
        assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testMoneyAssumptionDev2() {
        //Account account = new Account("Adrian", new BigDecimal("1000.12345"));
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(isDev, () -> { //only execute asserts
            assertEquals(1000.12345, account.getMoney().doubleValue());
            assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
        });
        assertEquals(1000.12345, account.getMoney().doubleValue());
        assertFalse(account.getMoney().compareTo(BigDecimal.ZERO) < 0);
    }

    @Nested
    @Tag("timeout")
    class TimeOutTests {
        @Test
        @Timeout(5)
        void testTimeOut() throws InterruptedException{
            TimeUnit.SECONDS.sleep(6);
        }

        @Test
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
        void testTimeOut2() throws InterruptedException{
            TimeUnit.SECONDS.sleep(6);
        }

        @Test
        void assertTimeOut() {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.SECONDS.sleep(6);
            });
        }
    }

}