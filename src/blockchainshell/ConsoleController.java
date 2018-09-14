package blockchainshell;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class ConsoleController {

    private Client cc;
    
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea chatArea;

    @FXML
    private Button connector;

    @FXML
    private TextField IPAdd, port;

    @FXML
    private Button send;

    @FXML
    private TextField recPubKey, amount;

    @FXML
    void connectToServer(ActionEvent event) {
        cc.connectToServer(IPAdd.getText(), port.getText());
        String clientName = getName();
        cc.getStream();
        cc.connect(chatArea, clientName);
        send.setDisable(false);
    }

    @FXML
    void sendBlock(ActionEvent event) {
        cc.chat();
    }

    public String getName() {
        System.out.println("In call ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Welcome to PolyChain");
        dialog.setHeaderText("Screen name selection");
        dialog.setContentText("Please Choose a Name");
        dialog.setGraphic(null);
        return dialog.showAndWait().get();
    }
    
    @FXML
    void initialize() {
        assert chatArea != null : "fx:id=\"chain\" was not injected: check your FXML file 'Console.fxml'.";
        assert connector != null : "fx:id=\"connector\" was not injected: check your FXML file 'Console.fxml'.";
        assert IPAdd != null : "fx:id=\"IPAdd\" was not injected: check your FXML file 'Console.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'Console.fxml'.";
        assert send != null : "fx:id=\"send\" was not injected: check your FXML file 'Console.fxml'.";
        assert recPubKey != null : "fx:id=\"recPubKey\" was not injected: check your FXML file 'Console.fxml'.";
        assert amount != null : "fx:id=\"amount\" was not injected: check your FXML file 'Console.fxml'.";
        
        cc = new Client();
        send.setDisable(true);
    }
}
