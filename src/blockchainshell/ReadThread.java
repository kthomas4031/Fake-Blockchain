/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javafx.scene.control.TextArea;

/**
 *
 * @author Dean
 */
public class ReadThread extends Thread {
    private ObjectInputStream reader;
    private Socket socket;
    private Client client;
    private TextArea tf;
 
    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
 
        try { 
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
        while (true) {
            try {
                //Gets object from server
                Object read = reader.readObject();
                
                //Tests what kind of object it is
                if (read instanceof String){
                    client.output((String)read);
                }else if(read instanceof Block){
                    PolyChain temp = client.getChain(); //SO COSTLY PLEASE LET ME USE MY FUNCTION
                    temp.addBlock((Block)read);
                    if (temp.isChainValid()){
                        System.out.println("\nBlock Added");
                        client.getChain().addBlock((Block)read);
                        client.output(((Block)read).toString() + "\n");
                    }else
                        client.output("\nBlock Invalid");
                }else if(read instanceof PolyChain){
                    if (((PolyChain) read).isChainValid()){
                        client.setChain((PolyChain)read);
                        client.output("\nBlock Chain Acquired");
                        client.getChain().printBlock();
                    } else
                        client.output("\nBlock Chain Invalid");
                }else{
                    System.out.println("\nServer sent something I'm too dumb to understand.");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            } catch (ClassNotFoundException ex) {
            }
        }
    }
}