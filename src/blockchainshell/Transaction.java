/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.Serializable;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 *
 * @author Kyle
 */
public class Transaction implements Serializable{
    
    public PublicKey pubKey, recPubKey;
    private PrivateKey privKey;
    private String amount;
    private KeyPairGenerator keyGenerator;
    private SecureRandom rando;
    private Signature privateSignature;
    
    public Transaction() {
        amount = "0";
    }
    
    public Transaction(String amount, String rec) {
        this.amount = amount;
        generateKeys(rec);
        try {
            encryptAmount();
        } catch (Exception ex) {
            System.out.println("Error with Encryption");
        }
    }
    
    
    private void generateKeys(String stored){
        
        //Parses the input Public Key String and conversts it into an actual Public Key
        try {
            stored = stored.replaceAll("\\n", "");
            byte[] data = Base64.getDecoder().decode((stored.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            recPubKey = fact.generatePublic(spec);
        } catch (GeneralSecurityException ex) {
            System.out.println("Issue with recipients Public Key");
        }
        
        //Creates a key generator and initializes it to a random number
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
            rando = new SecureRandom();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error with Key Generator");
        }
        keyGenerator.initialize(1024, rando);
        
        //Uses the key generator to create a public and private key for the sender 
        //This should not be done in the transaction class because it gives the sender a new key each time, 
        //but we don't have wallets or a database for the public keys so...
        KeyPair setMatch = keyGenerator.generateKeyPair();
        pubKey = setMatch.getPublic();
        privKey = setMatch.getPrivate();
        try {
            privateSignature = Signature.getInstance("RSA");
             privateSignature.initSign(privKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            System.out.println("Error with Signature");
        }
    }
    private void encryptAmount() throws Exception{
            //Encrypt using Receiver's public key first
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, recPubKey);
            byte[] cipherText = encryptCipher.doFinal(amount.getBytes(UTF_8));
            amount = Base64.getEncoder().encodeToString(cipherText);
            
            //Then sign using my private key
            privateSignature.update(amount.getBytes(UTF_8));
            byte[] signature = privateSignature.sign();
            amount = Base64.getEncoder().encodeToString(signature);
            
            //To decrypt, use my public key, and then use your private key.
    }

    @Override
    public String toString() {
        return ("Senders Public Key: " + pubKey + "\nAmount: " + amount + "\nRecipient's Public Key: " + recPubKey);
    }
    
    
}
