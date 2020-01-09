package tests;

import classes.HexNum;
//import utils.Hash;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class HexNumTests {

    public static void tests() throws NoSuchAlgorithmException {

        // Conversion Tests

    /*
        String str = "A1B2C3";
        byte[] byteArr = { (byte) 0xA1, (byte) 0xB2, (byte) 0xC3 };
        BigInteger bigInt = new BigInteger("10597059");

        HexNum stringInput = new HexNum(str);
        HexNum bytesInput = new HexNum(byteArr);
        HexNum bigIntInput = new HexNum(bigInt);

        System.out.println(stringInput.hexString);
        System.out.println(bytesInput.hexString);
        System.out.println(bigIntInput.hexString);

        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(stringInput.bytes)));
        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(bytesInput.bytes)));
        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(bigIntInput.bytes)));

        System.out.println(stringInput.scalar);
        System.out.println(bytesInput.scalar);
        System.out.println(bigIntInput.scalar);

        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(HexNum.hexToBytes(str))));
        System.out.println(HexNum.hexToScalar(str));

        System.out.println(HexNum.bytesToHex(byteArr));
        System.out.println(HexNum.bytesToScalar(byteArr));

        System.out.println(HexNum.scalarToHex(bigInt));
        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(HexNum.scalarToBytes(bigInt))));
    */

        /* Test Space */

//        System.out.println(ECC.p.mod(new HexNum("04")).hexString);
//        System.out.println(
//                new HexNum("0A").add(new HexNum("06"), new HexNum("FF")).hexString
//        );


        // Math Tests

        HexNum a = new HexNum("8AB");
        HexNum b = new HexNum("B78");

        // Mod
//        HexNum.setModulus(new BigInteger("4"));
//        HexNum n = new HexNum("B");
//        System.out.println(n.mod());
//        HexNum.resetModulus();

        // Addition
        // 1423
        HexNum c = a.add(b);
        System.out.println(c);
        System.out.println(b.add(a));

        // Negation
        System.out.println(a.neg());

        // Subtraction
        // B78, 8AB
        System.out.println(c.sub(a));
        System.out.println(c.sub(b));

        // Multiplication
        // 636928
        HexNum d = a.mul(b);
        System.out.println(d);
        System.out.println(b.mul(a));

        // Inverse
        System.out.println(a.inv());

        // Division
        System.out.println(d.div(a));
        System.out.println(d.div(b));

        // Exponentiation
        HexNum e = a.pow(b);
        System.out.println(e);

        // Square Root
        HexNum f = new HexNum("207DCBAC971F9691B1652951FEE5FC8AB9DC1754DDD61A99A60422265FFA9612");
        HexNum two = new HexNum("2");
        HexNum g = f.pow(two);
        System.out.println(g);
        HexNum[] h = g.sqrt();
        System.out.println(h[0] + "\n" + h[1]);
    }

}
