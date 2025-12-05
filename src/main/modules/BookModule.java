package main.modules;

import main.models.Book;
import main.utils.FileManager;

import java.util.List;
import java.util.Scanner;

public class BookModule {

    public void menu() {
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== MENU BUKU ===");
            System.out.println("1. Tampilkan Buku");
            System.out.println("2. Tambah Buku");
            System.out.println("3. Edit Buku");
            System.out.println("4. Hapus Buku");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1 -> showBooks();
                case 2 -> addBook();
                case 3 -> editBook();
                case 4 -> deleteBook();
            }
        } while (choice != 0);
    }

    public void showBooks() {
        List<Book> books = FileManager.loadBooks();
        System.out.println("\nDaftar Buku:");
        if (books.isEmpty()) System.out.println("(kosong)");
        for (Book b : books) System.out.println(b);
    }

    public void addBook() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Buku: ");
        String id = s.nextLine();
        System.out.print("Judul: ");
        String title = s.nextLine();
        System.out.print("Penulis: ");
        String author = s.nextLine();
        System.out.print("Stok: ");
        int stock = Integer.parseInt(s.nextLine());

        List<Book> books = FileManager.loadBooks();
        books.add(new Book(id, title, author, stock));
        FileManager.saveBooks(books);
        System.out.println("Buku ditambahkan.");
    }

    public void editBook() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Buku yang ingin diubah: ");
        String id = s.nextLine();
        List<Book> books = FileManager.loadBooks();
        Book found = null;
        for (Book b : books) if (b.getId().equals(id)) { found = b; break; }
        if (found == null) { System.out.println("Buku tidak ditemukan."); return; }
        System.out.print("Judul baru (enter=tidak berubah): ");
        String title = s.nextLine(); if (!title.isBlank()) found.setTitle(title);
        System.out.print("Penulis baru (enter=tidak berubah): ");
        String author = s.nextLine(); if (!author.isBlank()) found.setAuthor(author);
        System.out.print("Stok baru (enter=tidak berubah): ");
        String st = s.nextLine(); if (!st.isBlank()) found.setStock(Integer.parseInt(st));
        FileManager.saveBooks(books);
        System.out.println("Buku diperbarui.");
    }

    public void deleteBook() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Buku yang ingin dihapus: ");
        String id = s.nextLine();
        List<Book> books = FileManager.loadBooks();
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        if (removed) {
            FileManager.saveBooks(books);
            System.out.println("Buku dihapus.");
        } else System.out.println("Buku tidak ditemukan.");
    }
}
