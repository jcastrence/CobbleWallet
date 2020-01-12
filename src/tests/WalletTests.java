package tests;

import classes.Wallet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WalletTests {

    public static void tests() throws IOException, NoSuchAlgorithmException {

        // Set directory
        String homeDir = System.getProperty("user.dir");
        String path = homeDir + "/data/wallet.txt";
        // Set password
        String password = "butterfly door brick house";
        Wallet wallet = new Wallet(path, password);
        System.out.println(wallet);

        // Add a new key
        String newKey1 = "5KUENuU3h2DfTgBHV5Yr4NZYQdo93fjnTnCa9M3X4LisGpNdfnW";
        wallet.addKey(newKey1);
        System.out.println(wallet);

        // Add a key that already exists
        String newKey2 = "KzFZxB8LoHmWXcdyMbGuqbZDa5ebYYAj3VwaD9DhySicYUC1RA8B";
        wallet.addKey(newKey2);
        System.out.println(wallet);

    }

}
