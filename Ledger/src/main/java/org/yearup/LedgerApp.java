package org.yearup;

import java.math.BigDecimal;
import java.util.Scanner;

public class LedgerApp {
    public static void main(String[] args) {
        LedgerApp ledgerApp = new LedgerApp();
        ledgerApp.run();
    }

    public void run() {
        TransactionManager transactionManager = new TransactionManager();
        boolean running = true;

        while (running) {
            System.out.println("Menu:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("B) Show Total Balance");
            System.out.println("C) Clear Transactions");
            System.out.println("X) Exit");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    System.out.println("Enter deposit amount: ");
                    BigDecimal depositAmount = new BigDecimal(scanner.nextLine());
                    transactionManager.addDeposit(depositAmount);
                    System.out.println("You have successfully deposited $" + depositAmount + "!");
                    break;
                case "P":
                    System.out.println("Enter payment amount: ");
                    BigDecimal paymentAmount = new BigDecimal(scanner.nextLine()).negate();
                    transactionManager.addPayment(paymentAmount);
                    System.out.println("You have successfully made a payment of $" + paymentAmount.negate() + "!");
                    break;
                case "L":
                    transactionManager.showLedger();
                    break;
                case "B":
                    transactionManager.showTotalBalance();
                    break;
                case "C":
                    transactionManager.clearTransactions();
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
