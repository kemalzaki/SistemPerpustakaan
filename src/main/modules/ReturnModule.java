package main.modules;

import main.models.Book;
import main.models.Member;
import main.models.Transaction;
import main.utils.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReturnModule {

    public void menu(String memberId) {
        Scanner s = new Scanner(System.in);
        System.out.println("\n=== KEMBALIKAN BUKU ===");
        
        // 1. LOAD DATA TERBARU
        List<Transaction> transactions = FileManager.loadTransactions();
        List<Member> members = FileManager.loadMembers();
        List<Book> books = FileManager.loadBooks();

        // 2. TAMPILKAN DAFTAR PEMINJAMAN AKTIF
        boolean adaPinjaman = false;
        System.out.println("Transaksi dipinjam:");
        
        for (Transaction t : transactions) {
            // Hanya tampilkan yang statusnya BORROWED
            if ("BORROWED".equals(t.getStatus())) {
                // Filter: Jika Admin (null) tampilkan semua, Jika Member tampilkan punya sendiri
                if (memberId == null || memberId.equals(t.getMemberId())) {
                    System.out.println(t.getId() + " | member:" + t.getMemberId() + " | book:" + t.getBookId() + " | due:" + t.getDueDate());
                    adaPinjaman = true;
                }
            }
        }

        if (!adaPinjaman) {
            System.out.println("(tidak ada transaksi aktif)");
            return;
        }

        System.out.print("Masukkan ID Transaksi (atau masukkan ID Buku untuk mencari): ");
        String inputId = s.nextLine();

        // 3. LOGIKA PENCARIAN
        Transaction found = null;

        // A. Coba cari persis ID Transaksi (Prioritas Utama)
        for (Transaction t : transactions) {
            if (t.getId().equalsIgnoreCase(inputId) && "BORROWED".equals(t.getStatus())) {
                found = t;
                break;
            }
        }

        // B. Fallback: Cari ID Buku (Jika input bukan ID Transaksi)
        if (found == null) {
            List<Transaction> candidates = new ArrayList<>();
            
            // Kumpulkan semua transaksi aktif untuk buku tersebut
            for (Transaction t : transactions) {
                if (t.getBookId().equalsIgnoreCase(inputId) && "BORROWED".equals(t.getStatus())) {
                    if (memberId == null || memberId.equals(t.getMemberId())) {
                        candidates.add(t);
                    }
                }
            }

            // Analisa hasil pencarian ID Buku
            if (candidates.size() == 1) {
                // Kasus: Cuma 1 orang yang pinjam, langsung pilih
                found = candidates.get(0);
            } else if (candidates.size() > 1) {
                // Kasus: BANYAK yang pinjam buku ini (Duplikat)
                System.out.println("\n[?] Ada " + candidates.size() + " transaksi aktif untuk buku '" + inputId + "':");
                
                // Tampilkan opsi agar user memilih ID Transaksi yang benar
                for (Transaction cand : candidates) {
                    String pNama = getMemberName(members, cand.getMemberId());
                    System.out.println("    -> ID Transaksi: " + cand.getId() + " | Peminjam: " + pNama + " (" + cand.getMemberId() + ")" + " | due:" + cand.getDueDate());
                }
                
                System.out.print("Mohon masukkan ID Transaksi yang spesifik dari daftar di atas: ");
                String exactId = s.nextLine();

                // Cari ulang berdasarkan ID Transaksi yang dipilih user
                for (Transaction cand : candidates) {
                    if (cand.getId().equalsIgnoreCase(exactId)) {
                        found = cand;
                        break;
                    }
                }
                
                if (found == null) {
                    System.out.println("[X] ID Transaksi yang dipilih salah atau tidak ada di daftar.");
                    return;
                }
            }
        }

        // 4. VALIDASI HASIL PENCARIAN
        if (found == null) {
            System.out.println("Transaksi tidak ditemukan.");
            return;
        }

        // Validasi Kepemilikan (Khusus Member)
        if (memberId != null && !found.getMemberId().equals(memberId)) {
            System.out.println("Anda hanya dapat mengembalikan transaksi milik Anda sendiri.");
            return;
        }

        // 5. KONFIRMASI & HITUNG DENDA
        String mName = getMemberName(members, found.getMemberId());
        
        // Cari objek buku untuk ambil judul
        Book bookObj = null; 
        for(Book b : books) if(b.getId().equals(found.getBookId())) { bookObj = b; break; }
        String bTitle = (bookObj != null) ? bookObj.getTitle() : found.getBookId();

        // --- LOGIKA HITUNG DENDA ---
        long denda = 0;
        long terlambat = 0;
        try {
            LocalDate dueDate = LocalDate.parse(found.getDueDate()); // Asumsi format yyyy-MM-dd
            LocalDate today = LocalDate.now();
            
            if (today.isAfter(dueDate)) {
                terlambat = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
                denda = terlambat * 2000; // Tarif denda Rp 2.000/hari
            }
        } catch (Exception e) {
            System.out.println("[!] Gagal menghitung tanggal (Format salah).");
        }

        System.out.println("\n>>> KONFIRMASI PENGEMBALIAN <<<");
        System.out.println("Buku       : " + bTitle);
        System.out.println("Peminjam   : " + mName);
        System.out.println("Jatuh Tempo: " + found.getDueDate());
        System.out.println("Hari ini   : " + LocalDate.now());
        
        // Tampilkan info jika ada denda
        if (denda > 0) {
            System.out.println("Keterlambatan: " + terlambat + " hari");
            System.out.println("TOTAL DENDA  : Rp " + denda); 
        } else {
            System.out.println("Status     : Tepat Waktu (Tidak ada denda)");
        }
        
        System.out.print("Proses pengembalian? (y/n): ");
        String confirm = s.nextLine();
        
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Proses dibatalkan.");
            return; 
        }

        // 6. EKSEKUSI (UPDATE DATA & SAVE)
        
        // A. Update Stok Buku
        if (bookObj != null) {
            bookObj.setStock(bookObj.getStock() + 1);
        }

        // B. Update Status Transaksi
        found.setReturnDate(LocalDate.now().toString());
        found.setStatus("RETURNED");

        // C. Simpan Perubahan ke File
        FileManager.saveBooks(books);
        FileManager.saveTransactions(transactions);
        
        System.out.println("Transaksi " + found.getId() + " berhasil dikembalikan.");
    }

    // Overload method untuk Admin (tanpa parameter)
    public void menu() {
        menu(null);
    }

    // --- Helper Method ---
    private String getMemberName(List<Member> list, String id) {
        for (Member m : list) {
            if (m.getId().equals(id)) return m.getName();
        }
        return id; // Jika nama tidak ketemu, kembalikan ID-nya saja
    }
}