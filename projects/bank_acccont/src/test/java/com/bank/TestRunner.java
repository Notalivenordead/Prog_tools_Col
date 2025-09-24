package com.bank;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты банковской системы
 * Демонстрирует сложные сценарии и взаимодействие между компонентами
 */
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRunner {

    private BankService bankService;
    private BankAccount account1;
    private BankAccount account2;

    @BeforeAll
    void setUpAll() {
        System.out.println("=== НАЧАЛО ИНТЕГРАЦИОННЫХ ТЕСТОВ ===");
    }

    @BeforeEach
    void setUp() {
        bankService = new BankService();
        System.out.println("\n--- Инициализация тестовых данных ---");
    }

    @AfterEach
    void tearDown() {
        System.out.println("--- Очистка после теста ---");
    }

    @AfterAll
    void tearDownAll() {
        System.out.println("=== ЗАВЕРШЕНИЕ ИНТЕГРАЦИОННЫХ ТЕСТОВ ===");
    }

    @Test
    @Order(1)
    @DisplayName("Интеграционный тест: полный цикл операций счета")
    void testCompleteAccountLifecycle() {
        System.out.println("Тест: Полный цикл операций счета");

        // Создание счета
        account1 = bankService.createAccount("10001", "Интеграционный Тест", 1000.0);
        assertNotNull(account1, "Счет должен быть создан");
        assertEquals(1000.0, account1.getBalance(), 0.001, "Начальный баланс корректен");

        // Пополнение
        account1.deposit(500.0);
        assertEquals(1500.0, account1.getBalance(), 0.001, "Баланс после пополнения корректен");

        // Снятие
        account1.withdraw(300.0);
        assertEquals(1200.0, account1.getBalance(), 0.001, "Баланс после снятия корректен");

        // Проверка истории операций
        var history = account1.getTransactionHistory();
        assertTrue(history.size() >= 3, "Должна быть история операций");
        assertTrue(history.get(history.size() - 1).contains("Withdrawn"), "Последняя операция - снятие");

        System.out.println("✓ Полный цикл операций завершен успешно");
    }

    @Test
    @Order(2)
    @DisplayName("Интеграционный тест: переводы между счетами через сервис")
    void testIntegratedTransferSystem() {
        System.out.println("Тест: Система переводов между счетами");

        // Создание двух счетов
        account1 = bankService.createAccount("20001", "Отправитель", 5000.0);
        account2 = bankService.createAccount("20002", "Получатель", 1000.0);

        double initialTotalBalance = bankService.getTotalBankBalance();

        // Перевод через сервис
        bankService.transfer("20001", "20002", 1500.0);

        // Проверка балансов
        assertEquals(3500.0, account1.getBalance(), 0.001, "Баланс отправителя корректен");
        assertEquals(2500.0, account2.getBalance(), 0.001, "Баланс получателя корректен");

        // Проверка сохранения общей суммы
        assertEquals(initialTotalBalance, bankService.getTotalBankBalance(), 0.001,
                "Общий баланс банка не должен меняться при переводах");

        System.out.println("✓ Система переводов работает корректно");
    }

    @Test
    @Order(3)
    @DisplayName("Интеграционный тест: обработка ошибок в сложных сценариях")
    void testErrorHandlingInComplexScenarios() {
        System.out.println("Тест: Обработка ошибок в сложных сценариях");

        account1 = bankService.createAccount("30001", "Тест Ошибок", 500.0);
        account2 = bankService.createAccount("30002", "Тест Ошибок 2", 100.0);

        // Попытка перевода суммы, превышающей баланс
        assertThrows(InsufficientFundsException.class, () -> {
            bankService.transfer("30001", "30002", 1000.0);
        }, "Должна быть ошибка недостаточных средств");

        // Проверка, что балансы не изменились после неудачного перевода
        assertEquals(500.0, account1.getBalance(), 0.001, "Баланс не должен измениться после ошибки");
        assertEquals(100.0, account2.getBalance(), 0.001, "Баланс не должен измениться после ошибки");

        // Попытка создания счета с дублирующим номером
        assertThrows(IllegalArgumentException.class, () -> {
            bankService.createAccount("30001", "Дубликат", 1000.0);
        }, "Должна быть ошибка дублирования номера счета");

        System.out.println("✓ Обработка ошибок работает корректно");
    }

    @Test
    @Order(4)
    @DisplayName("Интеграционный тест: производительность с множеством счетов")
    void testPerformanceWithMultipleAccounts() {
        System.out.println("Тест: Работа с множеством счетов");

        // Создание 10 счетов
        for (int i = 1; i <= 10; i++) {
            bankService.createAccount("ACC" + i, "Клиент " + i, i * 1000.0);
        }

        assertEquals(10, bankService.getAccountsCount(), "Должно быть 10 счетов");

        // Проверка общего баланса
        double expectedTotal = (1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10) * 1000.0;
        assertEquals(expectedTotal, bankService.getTotalBankBalance(), 0.001,
                "Общий баланс должен быть рассчитан корректно");

        // Серия переводов
        for (int i = 1; i < 10; i++) {
            bankService.transfer("ACC" + i, "ACC" + (i + 1), 100.0);
        }

        System.out.println("✓ Работа с множеством счетов завершена успешно");
    }

    @Test
    @Order(5)
    @DisplayName("Интеграционный тест: сложная бизнес-логика")
    @Timeout(5) // Тест должен завершиться за 5 секунд
    void testComplexBusinessLogic() {
        System.out.println("Тест: Сложная бизнес-логика");

        // Создание сложной структуры счетов
        BankAccount savings = bankService.createAccount("SAV001", "Сберегательный счет", 10000.0);
        BankAccount current = bankService.createAccount("CUR001", "Текущий счет", 5000.0);
        BankAccount investment = bankService.createAccount("INV001", "Инвестиционный счет", 20000.0);

        // Сложная последовательность операций
        savings.withdraw(3000.0);
        current.deposit(3000.0);

        bankService.transfer("CUR001", "INV001", 2000.0);
        investment.withdraw(5000.0);
        current.deposit(5000.0);

        // Проверка конечных балансов
        assertEquals(7000.0, savings.getBalance(), 0.001, "Сберегательный счет");
        assertEquals(11000.0, current.getBalance(), 0.001, "Текущий счет");
        assertEquals(17000.0, investment.getBalance(), 0.001, "Инвестиционный счет");

        // Проверка общей целостности
        double total = savings.getBalance() + current.getBalance() + investment.getBalance();
        assertEquals(35000.0, total, 0.001, "Общая сумма должна сохраняться");

        System.out.println("✓ Сложная бизнес-логика реализована корректно");
    }

    @Test
    @Order(6)
    @DisplayName("Интеграционный тест: пограничные случаи")
    void testEdgeCases() {
        System.out.println("Тест: Пограничные случаи");

        // Создание счета с нулевым балансом
        BankAccount zeroAccount = bankService.createAccount("ZERO01", "Нулевой счет", 0.0);
        assertEquals(0.0, zeroAccount.getBalance(), 0.001, "Нулевой баланс");

        // Пополнение на минимальную сумму
        zeroAccount.deposit(0.01);
        assertEquals(0.01, zeroAccount.getBalance(), 0.001, "Минимальное пополнение");

        // Снятие всей суммы
        zeroAccount.withdraw(0.01);
        assertEquals(0.0, zeroAccount.getBalance(), 0.001, "Полное снятие");

        // Создание счета с очень большим балансом
        BankAccount largeAccount = bankService.createAccount("LARGE01", "Большой счет", 1_000_000.0);
        assertEquals(1_000_000.0, largeAccount.getBalance(), 0.001, "Большой баланс");

        System.out.println("✓ Пограничные случаи обработаны корректно");
    }

    @Nested
    @DisplayName("Группа тестов для специфических сценариев")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SpecificScenariosTest {

        private BankService nestedBankService;

        @BeforeEach
        void setUpNested() {
            nestedBankService = new BankService();
            System.out.println("Подготовка вложенных тестов...");
        }

        @Test
        @DisplayName("Тест: цепочка переводов")
        void testTransferChain() {
            System.out.println("Тест цепочки переводов");

            // Создание цепочки счетов
            BankAccount a = nestedBankService.createAccount("A", "A", 1000.0);
            BankAccount b = nestedBankService.createAccount("B", "B", 1000.0);
            BankAccount c = nestedBankService.createAccount("C", "C", 1000.0);

            // Цепочка переводов: A -> B -> C
            nestedBankService.transfer("A", "B", 500.0);
            nestedBankService.transfer("B", "C", 300.0);

            assertEquals(500.0, a.getBalance(), 0.001);
            assertEquals(1200.0, b.getBalance(), 0.001);
            assertEquals(1300.0, c.getBalance(), 0.001);
        }

        @Test
        @DisplayName("Тест: циклические зависимости")
        void testCircularDependencies() {
            System.out.println("Тест циклических зависимостей");

            BankAccount x = nestedBankService.createAccount("X", "X", 1000.0);
            BankAccount y = nestedBankService.createAccount("Y", "Y", 1000.0);

            // Взаимные переводы
            nestedBankService.transfer("X", "Y", 200.0);
            nestedBankService.transfer("Y", "X", 100.0);

            assertEquals(900.0, x.getBalance(), 0.001);
            assertEquals(1100.0, y.getBalance(), 0.001);
        }
    }
}