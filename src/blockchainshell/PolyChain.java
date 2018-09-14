/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Kyle
 */
public class PolyChain implements Serializable{

    private final ArrayList<Block> blockchain;
    private static int difficulty = 3;
    static final long serialVersionUID = 129348938L;
    
    public PolyChain() {
        blockchain = new ArrayList<>();
    }
    
    public void addBlock(Block b){
        blockchain.add(b);
    }    
    
    //Validates that the latest block in the chain has the proper amount of zeros
    public void mine(){
        blockchain.get(blockchain.size()-1).mineBlock(difficulty);
    }
    
//    public boolean verifyBlock(Block b){
//        return (b.getPreviousHash().equals(blockchain.get(blockchain.size()-1).getHash()));
//    }
    
    public boolean isChainValid(){
        Block lastBlock;
        Block nextBlock;
        
        //Iterate through blocks in array list
        for(int i = 1 ; i < blockchain.size() ; i++){
            lastBlock = blockchain.get(i-1);
            nextBlock = blockchain.get(i);
            
            //compare stored current hash to the hash calculated again
            if(!nextBlock.getHash().equals(nextBlock.calculateHash())){
                System.out.println("Current Hashes not equal");
                return false;
            }
            
            //compare previous block's hash and with next block's stored previous hash
            if(!lastBlock.getHash().equals(nextBlock.getPreviousHash()) ) {
                    System.out.println("Previous Hashes not equal");
                    return false;
            }
        }
        return true;
    }
    
    //This really should be a to string method but you know. I can't change my functions or add new ones.
    public void printBlock() {
        for (Block b : blockchain) {
            System.out.println("Hash for Block " + (blockchain.indexOf(b) + 1) + ": " + b.getHash());
        }}

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }
       
}
