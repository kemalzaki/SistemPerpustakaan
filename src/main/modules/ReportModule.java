package main.modules;

import main.models.Transaction;
import main.utils.FileManager;

import java.util.List;

public class ReportModule {

    public void menu() {
        System.out.println("=== LAPORAN TRANSAKSI ===");
        List<Transaction> transactions = FileManager.loadTransactions();
        if (transactions.isEmpty()) System.out.println("(tidak ada transaksi)");
        for (Transaction t : transactions) {
            System.out.println(String.format("%s | member:%s | book:%s | borrow:%s | due:%s | return:%s | %s", t.getId(), t.getMemberId(), t.getBookId(), t.getBorrowDate(), t.getDueDate(), t.getReturnDate(), t.getStatus()));
        }
    }
}
