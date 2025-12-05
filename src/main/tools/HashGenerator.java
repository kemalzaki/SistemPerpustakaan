package main.tools;

import main.utils.PasswordUtils;

public class HashGenerator {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java main.tools.HashGenerator <password>");
            System.exit(2);
        }
        String pass = args[0];
        String salt = PasswordUtils.generateSalt();
        String hash = PasswordUtils.hash(pass.toCharArray(), salt);
        System.out.println(salt + "|" + hash);
    }
}
