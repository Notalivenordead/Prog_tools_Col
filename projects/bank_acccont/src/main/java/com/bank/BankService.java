import java.util.Scanner;

public class BankService {
    private Account[] accounts;

    public BankService() {
        accounts = new Account[10];
        for (int i = 0; i < 10; i++) {
            accounts[i] = new Account(i, 10000);
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int id;

        while (true) {
            System.out.print("Введите ID: ");
            id = scanner.nextInt();

            if (id < 0 || id >= accounts.length) {
                System.out.println("Некорректный ID. Попробуйте снова.");
                continue;
            }

            showMainMenu(id, scanner);
        }
    }

    private void showMainMenu(int id, Scanner scanner) {
        int choice;
        do {
            System.out.println("\nОсновное меню");
            System.out.println("1: проверить баланс счета");
            System.out.println("2: снять со счета");
            System.out.println("3: положить на счет");
            System.out.println("4: выйти");
            System.out.print("Введите пункт меню: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.printf("Баланс равен %.2f\n", accounts[id].getBalance());
                    break;
                case 2:
                    System.out.print("Введите сумму для снятия со счета: ");
                    double withdrawAmount = scanner.nextDouble();
                    accounts[id].withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Введите сумму для пополнения счета: ");
                    double depositAmount = scanner.nextDouble();
                    accounts[id].deposit(depositAmount);
                    break;
                case 4:
                    System.out.println("Выход из меню.");
                    break;
                default:
                    System.out.println("Неверный пункт меню.");
            }
        } while (choice != 4);
    }

    public static void main(String[] args) {
        BankService bankService = new BankService();
        bankService.start();
    }
}