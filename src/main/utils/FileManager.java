package main.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.data.models.Book;
import main.data.models.Member;
import main.data.models.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    private static final String BOOK_PATH = "src/main/data/books.json";
    private static final String MEMBER_PATH = "src/main/data/members.json";
    private static final String TRANSACTION_PATH = "src/main/data/transactions.json";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Book> loadBooks() {
        try {
            File file = new File(BOOK_PATH);
            if (!file.exists()) {
                return new ArrayList<>(); 
            }

            return mapper.readValue(file, new TypeReference<List<Book>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Member> loadMembers() {
        try {
            File file = new File(MEMBER_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<Member>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Transaction> loadTransactions() {
        try {
            File file = new File(TRANSACTION_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<Transaction>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static void saveBooks(List<Book> books) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(BOOK_PATH), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTransactions(List<Transaction> transactions) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(TRANSACTION_PATH), transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveMembers(List<Member> members) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MEMBER_PATH), members);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}