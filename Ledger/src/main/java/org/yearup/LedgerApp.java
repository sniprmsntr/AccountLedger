package org.yearup;
import java.math.BigDecimal;
import java.util.Scanner;

public class LedgerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();
        String input;

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) View Ledger");
            System.out.println("B) Show Total Balance");
            System.out.println("C) Clear Transactions");
            System.out.println("X) Exit");
            System.out.print("Enter your choice: ");

            input = scanner.nextLine();

            switch (input.toUpperCase()) {
                case "D":
                    System.out.print("Enter deposit amount: ");
                    BigDecimal depositAmount = scanner.nextBigDecimal();
                    scanner.nextLine();
                    manager.addDeposit(depositAmount);
                    System.out.println("You have successfully deposited $" + depositAmount + "!");
                    break;
                case "P":
                    System.out.print("Enter payment amount: ");
                    BigDecimal paymentAmount = scanner.nextBigDecimal();
                    scanner.nextLine();
                    System.out.print("Enter payment vendor: ");
                    String vendor = scanner.nextLine();
                    manager.addPayment(paymentAmount, vendor);
                    System.out.println("You have successfully made a payment of $" + paymentAmount + " to " + vendor + "!");
                    break;
                case "L":
                    manager.showLedger();
                    break;
                case "B":
                    manager.showTotalBalance();
                    break;
                case "C":
                    manager.clearTransactions();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

