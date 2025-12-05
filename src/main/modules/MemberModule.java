package main.modules;

import main.models.Member;
import main.utils.FileManager;

import java.util.List;
import java.util.Scanner;

public class MemberModule {

    public void menu() {
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== MENU ANGGOTA ===");
            System.out.println("1. Tampilkan Anggota");
            System.out.println("2. Tambah Anggota");
            System.out.println("3. Edit Anggota");
            System.out.println("4. Hapus Anggota");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1 -> showMembers();
                case 2 -> addMember();
                case 3 -> editMember();
                case 4 -> deleteMember();
            }
        } while (choice != 0);
    }

    public void showMembers() {
        List<Member> members = FileManager.loadMembers();
        System.out.println("\nDaftar Anggota:");
        if (members.isEmpty()) System.out.println("(kosong)");
        for (Member m : members) System.out.println(m);
    }

    public void addMember() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Anggota: ");
        String id = s.nextLine();
        System.out.print("Nama: ");
        String name = s.nextLine();
        System.out.print("Password: ");
        String pass = s.nextLine();
        String salt = main.utils.PasswordUtils.generateSalt();
        String hash = main.utils.PasswordUtils.hash(pass.toCharArray(), salt);
        List<Member> members = FileManager.loadMembers();
        members.add(new Member(id, name, salt, hash));
        FileManager.saveMembers(members);
        System.out.println("Anggota ditambahkan.");
    }

    public void editMember() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Anggota yang ingin diubah: ");
        String id = s.nextLine();
        List<Member> members = FileManager.loadMembers();
        Member found = null;
        for (Member m : members) if (m.getId().equals(id)) { found = m; break; }
        if (found == null) { System.out.println("Anggota tidak ditemukan."); return; }
        System.out.print("Nama baru (enter=tidak berubah): ");
        String name = s.nextLine(); if (!name.isBlank()) found.setName(name);
        System.out.print("Ubah password? (enter=tidak) ");
        String p = s.nextLine();
        if (!p.isBlank()) {
            String salt = main.utils.PasswordUtils.generateSalt();
            String hash = main.utils.PasswordUtils.hash(p.toCharArray(), salt);
            found.setSalt(salt);
            found.setPasswordHash(hash);
        }
        FileManager.saveMembers(members);
        System.out.println("Anggota diperbarui.");
    }

    public void deleteMember() {
        Scanner s = new Scanner(System.in);
        System.out.print("ID Anggota yang ingin dihapus: ");
        String id = s.nextLine();
        List<Member> members = FileManager.loadMembers();
        boolean removed = members.removeIf(m -> m.getId().equals(id));
        if (removed) {
            FileManager.saveMembers(members);
            System.out.println("Anggota dihapus.");
        } else System.out.println("Anggota tidak ditemukan.");
    }
}
