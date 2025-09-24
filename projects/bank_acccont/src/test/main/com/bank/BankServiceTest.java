package com.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {
    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankService = new BankService();
    }

    @Test
    void testCreateAccount() {
        BankAccount account = bankService.createAccount("111", "Alice", 1000.0);
        assertNotNull(account);
        assertEquals("111", account.getAccountNumber());
    }

    @Test
    void testCreateDuplicateAccount() {
        bankService.createAccount("111", "Alice", 1000.0);
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount("111", "Bob", 500.0));
    }

    @Test
    void testGetAccount() {
        bankService.createAccount("111", "Alice", 1000.0);
        BankAccount account = bankService.getAccount("111");
        assertNotNull(account);
        assertEquals("Alice", account.getOwnerName());
    }

    @Test
    void testGetNonExistentAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.getAccount("999"));
    }

    @Test
    void testTransferBetweenAccounts() {
        bankService.createAccount("111", "Alice", 1000.0);
        bankService.createAccount("222", "Bob", 500.0);

        bankService.transfer("111", "222", 300.0);

        assertEquals(700.0, bankService.getAccount("111").getBalance(), 0.001);
        assertEquals(800.0, bankService.getAccount("222").getBalance(), 0.001);
    }

    @Test
    void testGetTotalBankBalance() {
        bankService.createAccount("111", "Alice", 1000.0);
        bankService.createAccount("222", "Bob", 500.0);

        double totalBalance = bankService.getTotalBankBalance();
        assertEquals(1500.0, totalBalance, 0.001);
    }

    @Test
    void testGetAccountsCount() {
        assertEquals(0, bankService.getAccountsCount());
        bankService.createAccount("111", "Alice", 1000.0);
        assertEquals(1, bankService.getAccountsCount());
        bankService.createAccount("222", "Bob", 500.0);
        assertEquals(2, bankService.getAccountsCount());
    }
}