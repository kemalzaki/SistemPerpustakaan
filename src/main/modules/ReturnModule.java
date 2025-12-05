package main.modules;

import main.models.Book;
import main.models.Member;
import main.models.Transaction;
import main.utils.FileManager;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class ReturnModule {

    private Scanner scanner;
    private List<Transaction> transactionList;
    private List<Book> bookList;
    private List<Member> memberList;
    private static final long DENDA_PER_HARI = 2000;

    public ReturnModule(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        // Langkah 1: Refresh data agar selalu mendapat data terbaru dari JSON
        refreshData();

        System.out.println("\n=========================================");
        System.out.println("      MODUL PENGEMBALIAN & DENDA");
        System.out.println("=========================================");
        
        System.out.print("Masukkan ID Transaksi atau ID Buku: ");
        String inputId = scanner.nextLine();

        // Langkah 2: Cari transaksi yang aktif (belum dikembalikan)
        Transaction trx = cariTransaksi(inputId);

        if (trx == null) {
            System.out.println("[X] Data tidak ditemukan atau buku sudah dikembalikan.");
            return;
        }

        // Langkah 3: Ambil detail data (Nama Member, Denda)
        String namaMember = getMemberName(trx.getMemberId());
        long denda = hitungDenda(trx.getDueDate());
        String judulBuku = getBookTitle(trx.getBookId());

        // Langkah 4: Tampilkan Informasi ke Layar
        System.out.println("\n--- Rincian Pengembalian ---");
        System.out.println("ID Transaksi    : " + trx.getId());
        System.out.println("Peminjam        : " + namaMember + " (ID: " + trx.getMemberId() + ")");
        System.out.println("Buku            : " + judulBuku + " (ID: " + trx.getBookId() + ")");
        System.out.println("Jatuh Tempo     : " + trx.getDueDate());
        System.out.println("Tanggal Kembali : " + LocalDate.now());
        System.out.println("Total Denda     : Rp " + denda);
        System.out.println("----------------------------");

        // Langkah 5: Konfirmasi Eksekusi
        System.out.print("Proses pengembalian & update stok? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            lakukanPengembalian(trx);
        } else {
            System.out.println("[!] Proses dibatalkan.");
        }
    }


    private void refreshData() {
        this.transactionList = FileManager.loadTransactions();
        this.bookList = FileManager.loadBooks();
        this.memberList = FileManager.loadMembers();
    }

    private Transaction cariTransaksi(String inputId) {
        for (Transaction t : transactionList) {
            boolean matchId = t.getId().equalsIgnoreCase(id) || t.getBookId().equalsIgnoreCase(id);
            boolean isActive = "borrowed".equalsIgnoreCase(t.getStatus());

            if (matchId && isActive) {
                return t;
            }
        }
        return null;
    }

    private long hitungDenda(String dueDateString) {
        try {
            LocalDate dueDate = LocalDate.parse(dueDateString);
            LocalDate today = LocalDate.now();

            if (today.isAfter(dueDate)) {
                long daysLate = ChronoUnit.DAYS.between(dueDate, today);
                return daysLate * DENDA_PER_HARI;
            }
        } catch (DateTimeParseException e) {
            System.out.println("[!] Format tanggal salah di database: " + dueDateString);
        }
        return 0; // Tidak ada denda jika belum lewat atau error
    }

    private void lakukanPengembalian(Transaction t) {
        // A. Update Status Transaksi
        t.setStatus("returned");
        t.setReturnDate(LocalDate.now().toString());

        // B. Update Stok Buku (+1)
        boolean stockUpdated = false;
        for (Book b : bookList) {
            if (b.getId().equalsIgnoreCase(t.getBookId())) {
                b.setStock(b.getStock() + 1);
                stockUpdated = true;
                break;
            }
        }

        // C. Simpan Perubahan ke JSON
        FileManager.saveTransactions(transactionList);
        if (stockUpdated) {
            FileManager.saveBooks(bookList);
        }

        System.out.println("[V] Pengembalian BERHASIL! Data telah disimpan.");
    }


    private String getMemberName(String memberId) {
        if (memberList != null) {
            for (Member m : memberList) {
                if (m.getId().equalsIgnoreCase(memberId)) {
                    // Pastikan di class Member ada method getName()
                    return m.getName(); 
                }
            }
        }
        return "Member Tidak Dikenal";
    }

    private String getBookTitle(String bookId) {
        if (bookList != null) {
            for (Book b : bookList) {
                if (b.getId().equalsIgnoreCase(bookId)) {
                    return b.getTitle();
                }
            }
        }
        return "Judul Tidak Diketahui";
    }
}