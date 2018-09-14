/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;


import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.scene.control.TextArea;

public class Client {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private TextArea chfield;
    private PolyChain chain;
    private String clientName;

    public Client() {

    }

    public void connectToServer(String ip, String port) {
        System.out.println("trying to connect to server");
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
        }
        try {
            clientSocket = new Socket(address, Integer.parseInt(port));
        } catch (IOException ex) {
            System.out.println("Could not connect to server");
        }
    }

    //Creates input and output streams
    public void getStream() {
        System.out.println("trying to get streams");
        //Launch a listener thread
        new ReadThread(clientSocket, this).start();
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Output stream set");
        } catch (IOException ex) {
            System.out.println("Could not get streams");
        }
        System.out.println("Successfully got streams");
    }

    //Allows easier access to the text area in the GUI as well as stores the input username somewhere accessible
    public void connect(TextArea mychat, String name) {
        System.out.println("Connecting Area");
        chfield = mychat;
        clientName = name;
        try {
            out.writeObject(name);
            out.flush();
        } catch (IOException ex) {
            System.out.println("error in chatting");
        }
    }
    
    public void post(Block serverResponse) {
        chain.addBlock(serverResponse);
    }
    
    public void output(String serverResponse) {
        chfield.appendText(serverResponse);
        System.out.println(serverResponse);
    }

    //Creates a new block, ensures it's hash is valid, and sends it to the server
    public void chat() {
        Block trade = new Block(clientName, chain.getBlockchain().get(chain.getBlockchain().size()-1).getHash());
        trade.mineBlock(3);
        PolyChain temp = chain; //SO COSTLY PLEASE LET ME USE MY FUNCTION
        temp.addBlock(trade);
        
        if (temp.isChainValid()){
            System.out.println("Sending Block to Server");
            try {
                out.writeObject(trade);

                out.flush();
            } catch (IOException e) {
                System.out.println("Block Failed to Send");
            }
        }else
            output("\nError in created block");
    }

    public PolyChain getChain() {
        return chain;
    }

    public TextArea getChfield() {
        return chfield;
    }

    public void setChain(PolyChain chain) {
        this.chain = chain;
    }
    
}
