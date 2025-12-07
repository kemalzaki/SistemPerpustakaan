package main.modules;

import main.models.Transaction;
import main.utils.FileManager;

import java.util.List;

public class ReportModule {

    // memberId == null -> show all transactions (admin)
    // memberId != null -> show only transactions for that member
    public void menu(String memberId) {
        System.out.println("=== LAPORAN TRANSAKSI ===");
        List<Transaction> transactions = FileManager.loadTransactions();
        if (transactions.isEmpty()) {
            System.out.println("(tidak ada transaksi)");
            return;
        }
        for (Transaction t : transactions) {
            if (memberId == null || memberId.equals(t.getMemberId())) {
                System.out.println(String.format("%s | member:%s | book:%s | borrow:%s | due:%s | return:%s | %s", t.getId(), t.getMemberId(), t.getBookId(), t.getBorrowDate(), t.getDueDate(), t.getReturnDate(), t.getStatus()));
            }
        }
    }

    // convenience for admin who calls without parameter
    public void menu() { menu(null); }
}
