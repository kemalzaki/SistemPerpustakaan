package main.tools;

import main.utils.FileManager;
import main.models.Member;
import main.utils.PasswordUtils;

import java.util.List;

public class TestAuth {
    public static void main(String[] args) {
        List<Member> members = FileManager.loadMembers();
        for (Member m : members) {
            boolean ok = PasswordUtils.verify("password123".toCharArray(), m.getSalt(), m.getPasswordHash());
            System.out.println(m.getId() + " verify password123 -> " + ok);
        }
    }
}
