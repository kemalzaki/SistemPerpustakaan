package main.modules;

import java.util.Scanner;

public class LoginModule {

    public boolean login() {
        Scanner input = new Scanner(System.in);
        System.out.print("Username: ");
        String user = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();

        return user.equals("admin") && pass.equals("admin");
    }
}
