package main.utils;

import main.models.Book;
import main.models.Member;
import main.models.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String BOOK_PATH = "src/main/data/books.txt";
    private static final String MEMBER_PATH = "src/main/data/members.txt";
    private static final String TRANSACTION_PATH = "src/main/data/transactions.txt";

    // Books: id|title|author|stock
    public static List<Book> loadBooks() {
        List<Book> list = new ArrayList<>();
        File file = new File(BOOK_PATH);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 4) {
                    Book b = new Book(p[0], p[1], p[2], Integer.parseInt(p[3]));
                    list.add(b);
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
        return list;
    }

    public static void saveBooks(List<Book> books) {
        File file = new File(BOOK_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Book b : books) {
                bw.write(String.format("%s|%s|%s|%d", b.getId(), b.getTitle(), b.getAuthor(), b.getStock()));
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    // Members: id|name
    public static List<Member> loadMembers() {
        List<Member> list = new ArrayList<>();
        File file = new File(MEMBER_PATH);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 2) {
                    Member m = new Member(p[0], p[1]);
                    list.add(m);
                }
            }
        } catch (IOException ignored) {
        }
        return list;
    }

    public static void saveMembers(List<Member> members) {
        File file = new File(MEMBER_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Member m : members) {
                bw.write(String.format("%s|%s", m.getId(), m.getName()));
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    // Transactions: id|memberId|bookId|borrowDate|dueDate|returnDate|status
    public static List<Transaction> loadTransactions() {
        List<Transaction> list = new ArrayList<>();
        File file = new File(TRANSACTION_PATH);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 7) {
                    Transaction t = new Transaction(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
                    list.add(t);
                }
            }
        } catch (IOException ignored) {
        }
        return list;
    }

    public static void saveTransactions(List<Transaction> transactions) {
        File file = new File(TRANSACTION_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Transaction t : transactions) {
                bw.write(String.format("%s|%s|%s|%s|%s|%s|%s", t.getId(), t.getMemberId(), t.getBookId(), t.getBorrowDate(), t.getDueDate(), t.getReturnDate()==null?"":t.getReturnDate(), t.getStatus()));
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }
}