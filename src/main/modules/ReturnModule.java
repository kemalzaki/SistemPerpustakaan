package main.modules;

import main.models.Book;
import main.models.Transaction;
import main.utils.FileManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReturnModule {

    // memberId == null -> admin/operator (can return for any member)
    public void menu(String memberId) {
        Scanner s = new Scanner(System.in);
        System.out.println("=== KEMBALIKAN BUKU ===");
        List<Transaction> transactions = FileManager.loadTransactions();
        if (transactions.isEmpty()) {
            System.out.println("(tidak ada transaksi)");
            return;
        }

        System.out.println("Transaksi dipinjam:");
        for (Transaction t : transactions) {
            if ("BORROWED".equals(t.getStatus())) {
                if (memberId == null || memberId.equals(t.getMemberId())) {
                    System.out.println(t.getId() + " | member:" + t.getMemberId() + " | book:" + t.getBookId() + " | due:" + t.getDueDate());
                }
            }
        }

        System.out.print("Masukkan ID Transaksi (atau masukkan ID Buku untuk mencari): ");
        String inputId = s.nextLine();
        Transaction found = null;
        for (Transaction t : transactions) if (t.getId().equals(inputId)) { found = t; break; }

        // fallback: if not found, try search by bookId (first BORROWED match)
        if (found == null) {
            for (Transaction t : transactions) {
                if ("BORROWED".equals(t.getStatus()) && t.getBookId().equals(inputId)) {
                    if (memberId == null || memberId.equals(t.getMemberId())) { found = t; break; }
                }
            }
        }

        if (found == null) { System.out.println("Transaksi tidak ditemukan."); return; }
        if (memberId != null && !memberId.equals(found.getMemberId())) { System.out.println("Anda hanya dapat mengembalikan transaksi milik Anda sendiri."); return; }

        // find book
        List<Book> books = FileManager.loadBooks();
        Book book = null;
        for (Book b : books) if (b.getId().equals(found.getBookId())) { book = b; break; }
        if (book != null) book.setStock(book.getStock() + 1);

        found.setReturnDate(LocalDate.now().toString());
        found.setStatus("RETURNED");

        FileManager.saveBooks(books);
        FileManager.saveTransactions(transactions);
        System.out.println("Transaksi " + found.getId() + " berhasil dikembalikan.");
    }

    // convenience for admin who wants to call without parameter
    public void menu() { menu(null); }
}
