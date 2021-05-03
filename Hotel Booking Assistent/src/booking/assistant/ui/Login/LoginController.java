/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.Login;

import booking.assistant.ui.MainWindow.MainWindowController;
import booking.assistant.ui.Settings.preferences;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Nelly
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField username;
    @FXML
    private PasswordField Password;

    /**
     * Initializes the controller class.
     */
    preferences pref;
    @FXML
    private Label header;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pref= preferences.getpreferences();
        
    }    

    @FXML
    private void loginOperation(ActionEvent event) {
        header.setText("Hotel Booking Admin Login");// #e7e7e7
         header.setStyle("-fx-background-color:#e7e7e7");
        String uname= username.getText();
        String pswd = DigestUtils.sha1Hex(Password.getText()) ;
        
        if(uname.equals(pref.getUsername()) && pswd.equals(pref.getPassword())){
            //load the main window
            closeStage();
            loadWindow("/booking/assistant/ui/MainWindow/MainWindow.fxml", "Hotel Booking Assistant Main Window");
          
        }
        else{
          header.setText("INVALID CREDENTIALS");
          header.setStyle("-fx-background-color:#d32f2f");
        }
    }
    
     void loadWindow(String loc,String title){   
        try {
               Parent parent = FXMLLoader.load(getClass().getResource(loc));
               Stage stage = new Stage(StageStyle.DECORATED);
               stage.setTitle(title);
               stage.setScene(new Scene(parent));
               stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } 
       }

    private void closeStage() {
       ((Stage)rootpane.getScene().getWindow()).close();
    }
}
