package tests;

import crypto.HexNum;
import crypto.AES;
import crypto.Hash;

import java.security.NoSuchAlgorithmException;

public class AESTests {

    public static void tests() throws NoSuchAlgorithmException {

        String msg1 = "5Km2kuu7vtFDPpxywn4u3NLpbr5jKpTB3jsuDU2KYEqetqj84qw";
        String msg2 = "5J4bXidtn3nYBZpirwXZEgjJWSjYtPxyzhxkasoQPw7cG4bi53w";
        String msg3 = "L24XEfJFDqHN5BU3MDvRdqnW9PCcuM6ehHfhqvsSAovufsWn1AGP";

        String key = "ian";

        String cip1 = AES.encrypt(msg1, key);
        String cip2 = AES.encrypt(msg2, key);
        String cip3 = AES.encrypt(msg3, key);

        System.out.println("Encrypted Keys");
        System.out.println(cip1);
        System.out.println(cip2);
        System.out.println(cip3);
        System.out.println("Decrypted Keys");
        System.out.println(AES.decrypt(cip1, key));
        System.out.println(AES.decrypt(cip2, key));
        System.out.println(AES.decrypt(cip3, key));
        System.out.println("Password Hash");
        System.out.println(HexNum.bytesToHex(Hash.doubleSHA(key)));

    }

}
