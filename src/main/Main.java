package main;

import classes.*;
import tests.*;
import crypto.*;
import java.io.*;
import java.security.*;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        userLogin();
//        tests();

    }

    public static void userLogin() throws IOException, NoSuchAlgorithmException {
        // Password and wallet file paths
        String passwordFilePath = System.getProperty("user.dir") + "/data/password.txt";
        String walletFilePath = System.getProperty("user.dir") + "/data/wallet.txt";
        // Log in user
        User user = new User(passwordFilePath, walletFilePath);
        user.mainMenu();
    }

    public static void tests() throws NoSuchAlgorithmException, IOException {

//        HexNumTests.tests();
//        EllipticCurveTests.tests();
//        KeyTests.tests();
        AESTests.tests();
//        WalletTests.tests();

    }

}