package modules;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import models.Member;
import utils.FileManager;

public class LoginModule {
    private Scanner scanner;
    private List<Member> members;

    public LoginModule() {
        scanner = new Scanner(System.in);
        // Load member data dari JSON
        members = FileManager.loadMembers("src/main/data/members.json");
    }

    // Fungsi login utama
    public Member login() {
        System.out.println("=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // Cek login admin
        if (username.equalsIgnoreCase("admin") && password.equals("admin123")) {
            System.out.println("Login berhasil sebagai ADMIN!");
            // Admin tidak perlu object Member, return null sebagai tanda admin
            return null;
        }

        // Cek login member
        for (Member member : members) {
            if (member.getUsername().equals(username) && member.getPassword().equals(password)) {
                System.out.println("Login berhasil sebagai MEMBER: " + member.getName());
                return member;
            }
        }

        System.out.println("Username atau password salah!");
        return null; // login gagal
    }

    // Fungsi helper untuk cek role
    public boolean isAdmin(Member member) {
        return member == null; // null menandakan admin
    }

    // Fungsi helper untuk login loop sampai sukses
    public Member loginLoop() {
        Member member = null;
        while (member == null) {
            member = login();
        }
        return member;
    }

    // Tes login module (optional)
    public static void main(String[] args) {
        LoginModule loginModule = new LoginModule();
        Member user = loginModule.loginLoop();

        if (loginModule.isAdmin(user)) {
            System.out.println("Akses penuh untuk ADMIN.");
        } else {
            System.out.println("Akses terbatas untuk MEMBER: " + user.getName());
        }
    }
}
