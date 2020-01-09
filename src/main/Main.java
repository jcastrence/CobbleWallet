package main;

import classes.EllipticCurve;
import classes.KeyPair;
import tests.*;

import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {

//        HexNumTests.tests();
//        KeyPairTests.tests();
//        EllipticCurveTests.tests();
        KeyPair testKey = new KeyPair();
        System.out.println(testKey.privateKey);
        System.out.println(testKey.publicKey);

    }

}