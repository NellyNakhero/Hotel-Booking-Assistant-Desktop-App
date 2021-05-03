/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.Settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nelly
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField Ammount_per_day;
    @FXML
    private TextField Username;
    @FXML
    private PasswordField password;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane rootpane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaults();
    }    

    @FXML
    private void LoadSaveAction(ActionEvent event) {
        float price= Float.parseFloat(Ammount_per_day.getText());
        String username= Username.getText();
        String pswd = password.getText();
        
        preferences pref= preferences.getpreferences();
        pref.setAmmoutPerDay(price);
        pref.setUsername(username);
        pref.setPassword(pswd);
        preferences.writePreferencesToFile(pref);
    }

    @FXML
    private void loadCancelOp(ActionEvent event) {
        Stage stage = (Stage)rootpane.getScene().getWindow();
        stage.close();
    }

    private void initDefaults() {
       preferences pref= preferences.getpreferences();
       Ammount_per_day.setText(String.valueOf(pref.getAmmoutPerDay()));
       Username.setText(String.valueOf(pref.getUsername()));
       password.setText(String.valueOf(pref.getPassword()));
    }
    
}
