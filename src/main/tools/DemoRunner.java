package main.tools;

import main.models.Book;
import main.models.Member;
import main.models.Transaction;
import main.utils.FileManager;
import main.utils.PasswordUtils;

import java.time.LocalDate;
import java.util.List;

public class DemoRunner {
    public static void main(String[] args) {
        System.out.println("=== DEMO RUNNER START ===");

        // Ensure a book exists
        List<Book> books = FileManager.loadBooks();
        boolean has = false;
        for (Book b : books) if (b.getId().equals("B001")) { has = true; break; }
        if (!has) {
            books.add(new Book("B001", "Clean Code", "Robert C. Martin", 3));
            FileManager.saveBooks(books);
            System.out.println("Added book B001");
        }

        // Ensure a member exists
        List<Member> members = FileManager.loadMembers();
        boolean hasM = false;
        for (Member m : members) if (m.getId().equals("M001")) { hasM = true; break; }
        if (!hasM) {
            String salt = PasswordUtils.generateSalt();
            String hash = PasswordUtils.hash("password123".toCharArray(), salt);
            members.add(new Member("M001", "Alice", salt, hash));
            FileManager.saveMembers(members);
            System.out.println("Added member M001");
        }

        // Borrow operation
        books = FileManager.loadBooks();
        members = FileManager.loadMembers();
        List<Transaction> transactions = FileManager.loadTransactions();

        Book book = null;
        for (Book b : books) if (b.getId().equals("B001")) { book = b; break; }
        Member member = null;
        for (Member m : members) if (m.getId().equals("M001")) { member = m; break; }

        if (book == null || member == null) {
            System.out.println("Missing book or member for demo.");
            return;
        }

        System.out.println("Before borrow: " + book);
        if (book.getStock() <= 0) {
            System.out.println("No stock to borrow.");
        } else {
            book.setStock(book.getStock() - 1);
            Transaction trx = new Transaction();
            trx.setId("TRX-" + System.currentTimeMillis());
            trx.setMemberId(member.getId());
            trx.setBookId(book.getId());
            trx.setBorrowDate(LocalDate.now().toString());
            trx.setDueDate(LocalDate.now().plusDays(7).toString());
            trx.setStatus("BORROWED");
            transactions.add(trx);
            FileManager.saveBooks(books);
            FileManager.saveTransactions(transactions);
            System.out.println("Borrowed: " + trx.getId());
        }

        System.out.println("Transactions after borrow:");
        for (Transaction t : FileManager.loadTransactions()) System.out.println(t.getId() + " " + t.getStatus());

        // Return: find BORROWED by member & book
        transactions = FileManager.loadTransactions();
        Transaction found = null;
        for (Transaction t : transactions) {
            if ("BORROWED".equals(t.getStatus()) && t.getMemberId().equals(member.getId()) && t.getBookId().equals(book.getId())) { found = t; break; }
        }
        if (found != null) {
            found.setReturnDate(LocalDate.now().toString());
            found.setStatus("RETURNED");
            // restore book
            for (Book b : books) if (b.getId().equals(found.getBookId())) { b.setStock(b.getStock() + 1); break; }
            FileManager.saveBooks(books);
            FileManager.saveTransactions(transactions);
            System.out.println("Returned: " + found.getId());
        }

        System.out.println("Transactions final:");
        for (Transaction t : FileManager.loadTransactions()) System.out.println(t.getId() + " " + t.getStatus());

        System.out.println("=== DEMO RUNNER END ===");
    }
}
