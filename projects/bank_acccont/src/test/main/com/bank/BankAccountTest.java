package com.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    private BankAccount account;
    private static final String ACCOUNT_NUMBER = "123456789";
    private static final String OWNER_NAME = "John Doe";
    private static final double INITIAL_BALANCE = 1000.0;

    @BeforeEach
    void setUp() {
        account = new BankAccount(ACCOUNT_NUMBER, OWNER_NAME, INITIAL_BALANCE);
    }

    @Test
    void testAccountCreation() {
        assertNotNull(account);
        assertEquals(ACCOUNT_NUMBER, account.getAccountNumber());
        assertEquals(OWNER_NAME, account.getOwnerName());
        assertEquals(INITIAL_BALANCE, account.getBalance(), 0.001);
    }

    @Test
    void testAccountCreationWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("999", "Test", -100));
    }

    @Test
    void testAccountCreationWithEmptyNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("", "Test", 100));
    }

    @Test
    void testDeposit() {
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance(), 0.001);
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(-100));
    }

    @Test
    void testWithdraw() {
        account.withdraw(300.0);
        assertEquals(700.0, account.getBalance(), 0.001);
    }

    @Test
    void testWithdrawInsufficientFunds() {
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(2000.0));
    }

    @Test
    void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(-100));
    }

    @Test
    void testTransfer() {
        BankAccount targetAccount = new BankAccount("987654321", "Jane Smith", 500.0);
        account.transfer(targetAccount, 300.0);

        assertEquals(700.0, account.getBalance(), 0.001);
        assertEquals(800.0, targetAccount.getBalance(), 0.001);
    }

    @Test
    void testTransferToSameAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> account.transfer(account, 100));
    }

    @Test
    void testTransactionHistory() {
        account.deposit(200.0);
        account.withdraw(100.0);

        List<String> history = account.getTransactionHistory();
        assertEquals(3, history.size()); // Initial + deposit + withdraw
        assertTrue(history.get(1).contains("Deposited: $200.00"));
        assertTrue(history.get(2).contains("Withdrawn: $100.00"));
    }

    @Test
    void testGetAccountInfo() {
        String info = account.getAccountInfo();
        assertTrue(info.contains(ACCOUNT_NUMBER));
        assertTrue(info.contains(OWNER_NAME));
        assertTrue(info.contains("1000.00"));
    }
}
