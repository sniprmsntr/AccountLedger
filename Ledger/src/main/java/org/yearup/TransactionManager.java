package org.yearup;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionManager {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private List<Transaction> transactions;
    private static final String ANSI_RED = "\033[31m";
    private static final String ANSI_RESET = "\033[0m";

    public TransactionManager() {
        createTransactionsFileIfNotExists();
        transactions = loadTransactions();
    }

    public void addDeposit(BigDecimal amount) {
        saveTransaction(new Transaction(LocalDate.now(), LocalTime.now(), "Deposit", "User", amount));
    }

    public void addPayment(BigDecimal amount, String vendor) {
        saveTransaction(new Transaction(LocalDate.now(), LocalTime.now(), "Payment", vendor, amount.negate()));
    }

    public void showLedger() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public void showTotalBalance() {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            total = total.add(transaction.getAmount());
        }
        System.out.println("Total Balance: " + total);
    }

    public void clearTransactions() {
        System.out.println(ANSI_RED + "Type 'CONFIRM' to clear all transactions (Step 1/2):" + ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        String confirmation1 = scanner.nextLine();

        if ("CONFIRM".equals(confirmation1)) {
            System.out.println(ANSI_RED + "Type 'CONFIRM' once more to clear all transactions (Step 2/2):" + ANSI_RESET);
            String confirmation2 = scanner.nextLine();

            if ("CONFIRM".equals(confirmation2)) {
                try {
                    Files.deleteIfExists(Paths.get(TRANSACTIONS_FILE));
                    transactions.clear();
                    createTransactionsFileIfNotExists();
                    System.out.println("All transactions cleared.");
                } catch (IOException e) {
                    System.err.println("Error clearing transactions: " + e.getMessage());
                }
            } else {
                System.out.println(ANSI_RED + "Confirmation failed at step 2. Transactions not cleared." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "Confirmation failed at step 1. Transactions not cleared." + ANSI_RESET);
        }
    }

    private void createTransactionsFileIfNotExists() {
        try {
            Path path = Paths.get(TRANSACTIONS_FILE);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.println("Error creating transactions file: " + e.getMessage());
        }
    }

    private List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                transactions.add(new Transaction(LocalDate.parse(data[0]), LocalTime.parse(data[1]),
                        data[2], data[3], new BigDecimal(data[4])));
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }

    private void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(transaction.toCSV());
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }
}
