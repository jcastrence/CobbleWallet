package classes;
import classes.Key;
import crypto.AES;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Wallet {

    private ArrayList<Key> keys = new ArrayList<Key>();
    private File walletFile;
    private String password;

    public Wallet(String walletFilePath, String password) throws IOException, NoSuchAlgorithmException {
        this.walletFile = new File(walletFilePath);
        this.password = password;
        importKeys();
    }

    // Import Bitcoin keys from a txt file
    public void importKeys() throws IOException, NoSuchAlgorithmException {
        System.out.println("Importing keys to wallet...");
        // Read from walletFile line by line
        try(BufferedReader reader = new BufferedReader(new FileReader(this.walletFile))) {
            String line = reader.readLine();
            // Check for end of file
            while(line != null) {
                // Check for empty line
                if (!line.trim().isEmpty()) {
                    // Strip line of whitepsace
                    String privateKeyWIFEncrypted = line.replaceAll("[\\n\\t ]", "");
                    // Decrypt AES encrypted key
                    String privateKeyWIF = AES.decrypt(privateKeyWIFEncrypted, this.password);
                    // Make a new key
                    Key privateKey = new Key(privateKeyWIF);
                    // Add key to wallet
                    this.keys.add(privateKey);
                }
                // Read next line
                line = reader.readLine();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw e;
        }
        System.out.println("All keys successfully imported!\n");
    }

    public void addKey(String privateKeyWIF) throws NoSuchAlgorithmException, IOException {
        Key privateKey = new Key(privateKeyWIF);
        // Check if key already exists in wallet
        if (this.keys.contains(privateKey)) {
            System.out.println("This key is already in your wallet!\n");
            return;
        }
        // Add key to key list
        keys.add(new Key(privateKeyWIF));
        // Encrypt key
        String encryptedPrivateKeyWIF = AES.encrypt(privateKeyWIF, this.password);
        // Add encrypted key to wallet.txt file
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.walletFile, true));
        writer.newLine();
        assert encryptedPrivateKeyWIF != null;
        writer.write(encryptedPrivateKeyWIF);
        System.out.println("New key added to wallet\n");
        writer.close();
    }

    public String toString() {
        if (keys.isEmpty()) {
            return "This wallet is empty!";
        }
        String str = "Private Key | Public Address | Type: \n";
        for (Key k: this.keys) {
            str += k.isCompressed() ?
                    k.getPrivateKeyWIFCompressed() + "\t\t" + k.getPublicAddressCompressed() + "\t\t\tCompressed\n":
                    k.getPrivateKeyWIFUncompressed() + "\t\t\t" + k.getPublicAddressUncompressed() + "\t\t\tUncompressed\n";
        }
        return str;
    }

}
