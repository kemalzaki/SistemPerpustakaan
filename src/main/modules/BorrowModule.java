package main.modules;

import main.data.models.Book;
import main.data.models.Member;
import main.data.models.Transaction;
import main.utils.FileManager;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowModule {
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== PINJAM BUKU ===");

        System.out.print("Masukkan ID Member: :");
        String memberId = scanner.nextLine();
        System.out.print("Masukkan ID Buku: ");
        String bookId = scanner.nextLine();

        List<Book> bookList = FileManager.loadBooks();
        List<Member> memberList = FileManager.loadMembers();
        List<Transaction> transactionList = FileManager.loadTransactions();
        
        Member foundMember = null;
        Book foundBook = null;
        
        for (Member m : memberList) {
            if (m.getId().equals(memberId)) {
                foundMember = m;
                break;
            }
        }
        for (Book b : bookList) {
            if (b.getId().equals(bookId)) {
                foundBook = b;
                break;
            }
        }   
        if (foundMember == null) {
            System.out.println("Member dengan ID " + memberId + " tidak ditemukan.");
            return;
        }
        if (foundBook == null) {
            System.out.println("Buku dengan ID " + bookId + " tidak ditemukan.");
            return;
        }
        if (foundBook.getStock() <= 0) {
            System.out.println("Stok buku " + foundBook.getTitle() + " tidak tersedia.");
            return;
        }

        foundBook.setStock(foundBook.getStock() - 1);

        Transaction trx = new Transaction();
        trx.setId("TRX-" + System.currentTimeMillis());
        trx.setMemberId(memberId);
        trx.setBookId(bookId);
        trx.setBorrowDate(LocalDate.now().toString());
        trx.setDueDate(LocalDate.now().plusDays(7).toString());
        trx.setStatus("BORROWED");

        transactionList.add(trx);

        FileManager.saveBooks(bookList);
        FileManager.saveTransactions(transactionList);

        System.out.println("Buku " + foundBook.getTitle() + " berhasil dipinjam oleh " + foundMember.getName() + ".");
        System.out.println("Tanggal Kembali: " + trx.getDueDate());
    }
}
