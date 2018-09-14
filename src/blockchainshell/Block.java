/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author Kyle
 */
public final class Block implements Serializable{

    public String hash;
    public String previousHash;
    private int nonce;
    private String data;
    private long timeStamp;
    private static final long serialversionUID = 129348939L;
    //private final Transaction trade;
    
     
    public Block(String data, String previousHash) {
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        //this.trade = trade;
        this.data = data;
        hash = calculateHash(); //Calculates an intial hash for the block that isn't tested for zeros
                                //I honestly think this should just be mineBlock. It takes out a few steps.
    }
    
    //Continue calculating a new hash for the block until the has begins with a certain number of zeros
    public String mineBlock(int zeros) {
            System.out.println("Mining Block...");
            String target = new String(new char[zeros]).replace('\0', '0'); //Create a string with difficulty * "0" 
            while(!hash.substring( 0, zeros).equals(target)) {
                    nonce++; //Increment to get a new hash
                    hash = applySHA256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
            }
            System.out.println("Block Hash: " + hash);

            return hash;
	}
    
    //Creates a hash for the block
    public String calculateHash() {
        return applySHA256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data );
    }
    
    //Uses standard SHA256 encryption. Helper function.
    public String applySHA256(String beginning) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input, 
            byte[] hash = digest.digest(beginning.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return ("Block hash: " + hash); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
