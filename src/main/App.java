package main;

import main.modules.BookModule;
import main.modules.BorrowModule;
import main.modules.LoginModule;
import main.modules.MemberModule;
import main.modules.ReportModule;
import main.modules.ReturnModule;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LoginModule login = new LoginModule();
        System.out.println("Sistem Perpustakaan");
        main.models.LoginResult result = login.login();
        if (result == null) {
            System.out.println("Login gagal. Keluar.");
            return;
        }

        BookModule bookModule = new BookModule();
        MemberModule memberModule = new MemberModule();
        BorrowModule borrowModule = new BorrowModule();
        ReturnModule returnModule = new ReturnModule();
        ReportModule reportModule = new ReportModule();

        if ("ADMIN".equals(result.getRole())) {
            int choice;
            do {
                System.out.println("\n=== MENU UTAMA (ADMIN) ===");
                System.out.println("1. Buku");
                System.out.println("2. Anggota");
                System.out.println("3. Pinjam Buku");
                System.out.println("4. Kembalikan Buku");
                System.out.println("5. Laporan");
                System.out.println("0. Keluar");
                System.out.print("Pilih: ");
                choice = Integer.parseInt(s.nextLine());

                switch (choice) {
                    case 1 -> bookModule.menu();
                    case 2 -> memberModule.menu();
                    case 3 -> borrowModule.menu(null);
                    case 4 -> returnModule.menu(null);
                    case 5 -> reportModule.menu();
                }
            } while (choice != 0);
        } else {
            // MEMBER
            String memberId = result.getMemberId();
            int choice;
            do {
                System.out.println("\n=== MENU UTAMA (MEMBER) ===");
                System.out.println("1. Lihat Buku");
                System.out.println("2. Pinjam Buku");
                System.out.println("3. Kembalikan Buku");
                System.out.println("4. Riwayat Saya");
                System.out.println("0. Keluar");
                System.out.print("Pilih: ");
                choice = Integer.parseInt(s.nextLine());
                switch (choice) {
                    case 1 -> bookModule.showBooks();
                    case 2 -> borrowModule.menu(memberId);
                    case 3 -> returnModule.menu(memberId);
                    case 4 -> reportModule.menu(memberId);
                }
            } while (choice != 0);
        }

        System.out.println("Terima kasih.");
    }
}
