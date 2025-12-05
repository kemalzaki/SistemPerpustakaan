package main.modules;

import main.models.Book;
import main.models.Transaction;
import main.utils.FileManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReturnModule {

    public void menu() {
        Scanner s = new Scanner(System.in);
        System.out.println("=== PENGEMBALIAN BUKU ===");
        System.out.print("Masukkan ID Transaksi: ");
        String trxId = s.nextLine();

        List<Transaction> transactions = FileManager.loadTransactions();
        Transaction found = null;
        for (Transaction t : transactions) if (t.getId().equals(trxId)) { found = t; break; }
        if (found == null) { System.out.println("Transaksi tidak ditemukan."); return; }
        if (!"BORROWED".equals(found.getStatus())) { System.out.println("Transaksi bukan status dipinjam."); return; }

        found.setReturnDate(LocalDate.now().toString());
        found.setStatus("RETURNED");

        // restore book stock
        List<Book> books = FileManager.loadBooks();
        for (Book b : books) if (b.getId().equals(found.getBookId())) { b.setStock(b.getStock()+1); break; }

        FileManager.saveTransactions(transactions);
        FileManager.saveBooks(books);
        System.out.println("Buku dikembalikan.");
    }
}
