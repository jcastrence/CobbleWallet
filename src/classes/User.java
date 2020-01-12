package classes;

import crypto.Hash;
import crypto.HexNum;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class User {

    private Scanner scanner;
    private String passwordFilePath;
    private String password;
    private String walletFilePath;
    private Wallet wallet;

    public User(String passwordFilePath, String walletFilePath) throws IOException, NoSuchAlgorithmException {
        this.scanner = new Scanner(System.in);
        this.passwordFilePath = passwordFilePath;
        this.walletFilePath = walletFilePath;
        login(this.passwordFilePath);
        this.wallet = new Wallet(this.walletFilePath, this.password);
    }

    public void login(String passwordFilePath) throws NoSuchAlgorithmException, IOException {
        String password;
        System.out.println("Welcome to Cobble Wallet!");
        // Check for existing password
        BufferedReader reader = new BufferedReader(new FileReader(passwordFilePath));
        String existingPassword = reader.readLine();
        // If this file is empty this is a new wallet; create a new password
        if (existingPassword == null || existingPassword.trim().isEmpty()) {
            String newPassword, confirmPassword;
            System.out.println("Please set a password for your wallet: ");
            newPassword = this.scanner.nextLine();
            System.out.println("Confirm your new password: ");
            confirmPassword = this.scanner.nextLine();
            // Password not confirmed
            while (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                System.out.println("Please set a password for your wallet: ");
                newPassword = this.scanner.nextLine();
                System.out.println("Confirm your new password: ");
                confirmPassword = this.scanner.nextLine();
            }
            // Password confirmed
            System.out.println("Password confirmed. Saving new password.");
            // Hash password
            String hashedPassword = HexNum.bytesToHex(Hash.doubleSHA(newPassword));
            // Save password to password.txt
            BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFilePath));
            writer.write(hashedPassword);
            writer.close();
            password = newPassword;
        }
        // If there is an existing password prompt the user for a password and check
        else {
            System.out.println("Please enter your password: ");
            String inputPassword = this.scanner.nextLine();
            // Hash password
            String hashPassword = HexNum.bytesToHex(Hash.doubleSHA(inputPassword));
            // Check input password against existing password
            while (!hashPassword.equals(existingPassword)) {
                System.out.println("The password you have entered is incorrect. Please try again:");
                inputPassword = this.scanner.nextLine();
                hashPassword = HexNum.bytesToHex(Hash.doubleSHA(inputPassword));
            }
            password = inputPassword;
        }
        System.out.println("Login successful!");
        reader.close();
        this.password = password;
    }



    public void mainMenu() throws NoSuchAlgorithmException, IOException {
        String input = "";
        // Display main menu
        System.out.println("You are now in the Main Menu\n");
        System.out.println("Commands:");
        System.out.println(" - view wallet");
        System.out.println(" - add key");
        System.out.println(" - change password");
        System.out.println(" - logout");
        System.out.println();
        // Main menu loop
        while(true) {
            // Wait for user input
            System.out.print("> ");
            input = this.scanner.nextLine();
            System.out.println();
            switch (input) {
                case "view wallet":
                    System.out.println(this.wallet);
                    break;
                case "add key":
                    System.out.println("Generating a new random key");
                    Key newKey = new Key();
                    System.out.println("Adding new key to wallet");
                    wallet.addKey(newKey.getPrivateKeyWIFCompressed());
                    break;
                case "change password":
                    System.out.println("Currently not yet implemented, check back soon!\n");
                    break;
                case "logout":
                    System.out.println("Logging out\n");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }


    }

}
