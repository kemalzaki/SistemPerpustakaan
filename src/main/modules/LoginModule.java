package main.modules;

import main.models.LoginResult;
import main.models.Member;
import main.utils.FileManager;
import main.utils.PasswordUtils;

import java.util.List;
import java.util.Scanner;

public class LoginModule {

    public LoginResult login() {
        Scanner input = new Scanner(System.in);
        System.out.print("Username: ");
        String user = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();

        // admin shortcut
        if (user.equals("admin") && pass.equals("admin")) {
            return new LoginResult("ADMIN", null);
        }

        // try member login by id and password
        List<Member> members = FileManager.loadMembers();
        for (Member m : members) {
            if (m.getId().equals(user)) {
                String salt = m.getSalt();
                String hash = m.getPasswordHash();
                if (salt != null && !salt.isBlank() && hash != null && !hash.isBlank()) {
                    boolean ok = PasswordUtils.verify(pass.toCharArray(), salt, hash);
                    if (ok) return new LoginResult("MEMBER", m.getId());
                }
            }
        }

        return null; // login gagal
    }
}
