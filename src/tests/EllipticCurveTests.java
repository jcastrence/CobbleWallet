package tests;

import classes.EllipticCurve;
import classes.HexNum;

public class EllipticCurveTests {

    public static void tests() {

//        EllipticCurve EC = new EllipticCurve("0", "7", "17", "1", "A", "17");
////        EC.doublePoint("1", "A");
////        EC.addPoints("1", "A", "16", "0B");
////        EC.addInfinity("16", "C");
//        EC.multiply("1A", "10", "14");
        EllipticCurve EC = new EllipticCurve();
        String privateKey = "9075BE5621317B148E6226BEA827D1427487D433B47D15BAD8176389FFE98617";
        String publicKey = EC.computePublicKey(privateKey);
        System.out.println(privateKey);
        System.out.println(publicKey);

    }

}
