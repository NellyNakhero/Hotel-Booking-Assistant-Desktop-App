/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.test;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author User
 */
public class TestingController implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField Ammount_per_day;
    @FXML
    private TextField Username;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         initDefaults();
    }    

    @FXML
    private void SaveOperation(ActionEvent event) {
        float price= Float.parseFloat(Ammount_per_day.getText());
        String username= Username.getText();
        String pswd = password.getText();
        
        preferences pref= preferences.getpreferences();
        pref.setAmmoutPerDay(price);
        pref.setUsername(username);
        pref.setPassword(pswd);
        preferences.writePreferencesToFile(pref);
    }
    
     private void initDefaults() {
       preferences pref= preferences.getpreferences();
       Ammount_per_day.setText(String.valueOf(pref.getAmmoutPerDay()));
       Username.setText(String.valueOf(pref.getUsername()));
       password.setText(String.valueOf(pref.getPassword()));
    }

    @FXML
    private void CancelOperations(ActionEvent event) {
    }
    
}
