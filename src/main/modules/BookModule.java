package modules;

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
            choice = input.nextInt();
            
            switch (choice) {
                case 1 -> showBooks();
                case 2 -> addBook();
                case 3 -> editBook();
                case 4 -> deleteBook();
            }
        } while (choice != 0);
    }

    public void showBooks() {}
    public void addBook() {}
    public void editBook() {}
    public void deleteBook() {}
}
