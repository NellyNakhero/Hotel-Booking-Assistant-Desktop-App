/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.AddGuest;

import booking.assistant.database.DatabaseHandler;
import booking.assistant.ui.ListsGuests.ListsGuestsController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Nelly
 */
public class AddGuestController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField Guest_Name;
    @FXML
    private TextField Guest_ID;
    @FXML
    private TextField Guest_Tel;
    @FXML
    private TextField Guest_Email;
    @FXML
    private TextField From_Date;
    @FXML
    private TextField ToDate;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    
    DatabaseHandler handler;
    
    private boolean isInEditMode= Boolean.FALSE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         handler= DatabaseHandler.getInstance();
          checkData();
    }    

    @FXML
    private void addGuest(ActionEvent event) {
        String  Name= Guest_Name.getText();
        String NationalID = Guest_ID.getText();
        String GuestTel = Guest_Tel.getText();
        String GuestEmail = Guest_Email.getText();
        String FromDate = From_Date.getText();
        String To_Date = ToDate.getText();
        
        Boolean flag= Name.isEmpty()||NationalID.isEmpty()||GuestTel.isEmpty()||GuestEmail.isEmpty()||FromDate.isEmpty()||To_Date.isEmpty();
        if(flag) {
          JOptionPane.showMessageDialog(null, "Error: please enter all fields", "Error Occured", JOptionPane.ERROR_MESSAGE);
        return;
        }
        
        if (isInEditMode){
          handleEditOperation();
          return;
      }
        //...............................................................................................................
        String str= "INSERT INTO GUESTS VALUES(" +
                "'"+ Name +"'," +
                "'"+ NationalID +"'," +
                "'"+ GuestTel +"'," +
                "'"+ GuestEmail +"'," +
                "'"+ FromDate +"'," +
                "'"+ To_Date + "'" +
                ")";
        System.out.println(str);
        //////PROBLEM AREA///////
       if( handler.execAction(str)){
             JOptionPane.showMessageDialog(null, "SUCCESS" , "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
           }
       else//error 
       {
             JOptionPane.showMessageDialog(null, "FAILED:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage)rootpane.getScene().getWindow();
        stage.close();
    }
    
//..............................................................................................................
    
    private void checkData() {
       String qur = "SELECT Name FROM GUESTS";
        ResultSet rs = handler.execQuery(qur);
        try {
            while (rs.next()) {
                String titlex = rs.getString("Name");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddGuestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//...............................................................................................................
    public void inflateUI(ListsGuestsController.Guest guest){
          Guest_Name.setText(guest.getGuest_Name());
          Guest_ID.setText(guest.getNational_ID());
          Guest_Tel.setText(guest.getTelephone());
          Guest_Email.setText(guest.getEmail());
          From_Date.setText(guest.getFdate());
          ToDate.setText(guest.getTdate());
          Guest_ID.setEditable(false);
          isInEditMode= Boolean.TRUE;
      }

    private void handleEditOperation() {
      ListsGuestsController.Guest guest = new ListsGuestsController.Guest(Guest_Name.getText(), Guest_ID.getText(), Guest_Tel.getText(), Guest_Email.getText(), From_Date.getText(), ToDate.getText());
      if(handler.updatingGuest(guest)){
       JOptionPane.showMessageDialog(null, "Guest is Updated Successfully","Success", JOptionPane.INFORMATION_MESSAGE);
      }
     else{
         JOptionPane.showMessageDialog(null, "Operation Update Guest Unsuccessful","Operation Failed",JOptionPane.ERROR_MESSAGE);
     }
    }
}
    
