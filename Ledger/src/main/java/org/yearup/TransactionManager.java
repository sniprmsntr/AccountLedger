package org.yearup;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private List<Transaction> transactions;

    public TransactionManager() {
        transactions = loadTransactions();
    }

    public void addDeposit(BigDecimal amount) {
        saveTransaction(new Transaction(LocalDate.now(), LocalTime.now(), "Deposit", "User", amount));
    }

    public void addPayment(BigDecimal amount) {
        saveTransaction(new Transaction(LocalDate.now(), LocalTime.now(), "Payment", "User", amount));
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
        try {
            Files.deleteIfExists(Paths.get(TRANSACTIONS_FILE));
            transactions.clear();
        } catch (IOException e) {
            System.err.println("Error clearing transactions: " + e.getMessage());
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
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(transaction.toCsv());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }
}
