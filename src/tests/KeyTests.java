package tests;

import java.security.NoSuchAlgorithmException;

import classes.Key;

public class KeyTests {

    public static void tests() throws NoSuchAlgorithmException {

//        HexNum privateKey1 = new HexNum("3D5080EDAA80223CF85DAF25D75438ADF3FFC9B7FBE69BF10E9484400D15633E");
//        HexNum privateKey2 = new HexNum("4BB95F98CBC825E67407E05EAF166A12CA53871B1124677D807C5C02F6620E34");
//        HexNum privateKey3 = new HexNum("3C2376FF679728F4DA95630A268BBD0FD5ADF90D3CDAF647F4BD354A368F7ED2");
//        HexNum privateKey4 = new HexNum("1111111111111111111111111111111111111111111111111111111111111111");
//        HexNum privateKey5 = new HexNum("73C89D6C48721B86C683643B094B3222331068A13846AD3475CDBF59394CE065");
//
//        Key testKey1 = new Key(privateKey1);
//        Key testKey2 = new Key(privateKey2);
//        Key testKey3 = new Key(privateKey3);
//        Key testKey4 = new Key(privateKey4);
//        Key testKey5 = new Key(privateKey5);
//
//        System.out.println(testKey1);
//        System.out.println(testKey2);
//        System.out.println(testKey3);
//        System.out.println(testKey4);
//        System.out.println(testKey5);

        String WIF1 = new String("5Km2kuu7vtFDPpxywn4u3NLpbr5jKpTB3jsuDU2KYEqetqj84qw");

        Key testKey6 = new Key(WIF1);

        System.out.println(testKey6);

//        HexNum privateKey6 = new HexNum("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140");
//
//        Key testKey6 = new Key(privateKey6);
//
//        System.out.println(testKey6);
    }

}
