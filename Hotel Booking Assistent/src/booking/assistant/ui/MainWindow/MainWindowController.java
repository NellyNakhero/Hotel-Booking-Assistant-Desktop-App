/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.MainWindow;

import booking.assistant.database.DatabaseHandler;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import org.apache.derby.database.Database;

/**
 * FXML Controller class
 *
 * @author Nelly
 */
public class MainWindowController implements Initializable {

    @FXML
    private HBox Book_Info;
    @FXML
    private HBox Guest_Info;
    @FXML
    private TextField Room_ID_Input;
    @FXML
    private Text Room_Service;
    @FXML
    private Text Availabilty;
    @FXML
    private Text Room_Floor;
    @FXML
    private TextField Guest_ID_Input;
    @FXML
    private Text Guest_Name;
    @FXML
    private Text Guset_Mobile;
    
    Boolean isReadyForSubmission = false;
    
    DatabaseHandler handler;
    
    @FXML
    private TextField Room_Info_ID;
    @FXML
    private ListView<String> Issue_Data_List;
    @FXML
    private Button settingsButton;
    @FXML
    private StackPane rootpane;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(Book_Info, 1);
        JFXDepthManager.setDepth(Guest_Info, 1);
        
        handler= DatabaseHandler.getInstance();
    }    

    @FXML
    private void loadAddRoom(ActionEvent event) {
        loadWindow("/booking/assistant/ui/AddRoom/AddRoom.fxml","Add New Room");
    }
    
     @FXML
    private void Load_Settings_Operation(ActionEvent event) {
        loadWindow("/booking/assistant/ui/Settings/settings.fxml","Settings");
    }

    @FXML
    private void loadAddGuest(ActionEvent event) {
         loadWindow("/booking/assistant/ui/AddGuest/AddGuest.fxml", "Add New Hotel Guest");
    }

    @FXML
    private void loadRoomsTable(ActionEvent event) {
        loadWindow("/booking/assistant/ui/ListRooms/ListRooms.fxml","All Hotel Rooms");
    }

    @FXML
    private void loadGuestsTable(ActionEvent event) {
        loadWindow("/booking/assistant/ui/ListsGuests/ListsGuests.fxml", "All Hotel Guests");
    }
//...............................................................................................................
       void loadWindow(String loc,String title){   
        try {
               Parent parent = FXMLLoader.load(getClass().getResource(loc));
               Stage stage = new Stage(StageStyle.DECORATED);
               stage.setTitle(title);
               stage.setScene(new Scene(parent));
               stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
       }

    @FXML
    private void Load_Room_Info(ActionEvent event) {
        String ID = Room_ID_Input.getText();
        String quer= "SELECT * FROM ROOMS WHERE ID = '"+ ID +"'";
        ResultSet rs = handler.execQuery(quer);
        Boolean flag = false;
        
        try {
            while(rs.next()){
                String Room_Flr = rs.getString("Floor");
                String Room_Serv = rs.getString("Room_Service_One");
                Boolean Status = rs.getBoolean("isAvail");
                
                Room_Floor.setText(Room_Flr);
                Room_Service.setText(Room_Serv);
                String status= (Status)? "Available":"Not Available";
                Availabilty.setText(status);
                
                flag = true;
            }
            if(!flag){
                Room_Floor.setText("No Such Room Available!");
                Room_Service.setText("");
                Availabilty.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void LoadGuestInfo(ActionEvent event) {
        String NationalID = Guest_ID_Input.getText();
        String quer= "SELECT * FROM GUESTS WHERE NationalID = '"+ NationalID +"'";
        ResultSet rs = handler.execQuery(quer);
        Boolean flag = false;
        
        try {
            while(rs.next()){
                String Telephone_Number = rs.getString("Telephone_Number");
                String Name = rs.getString("Name");
                
                Guest_Name.setText(Name);
                Guset_Mobile.setText(Telephone_Number);
                
                flag = true;
            }
            if(!flag){
               Guest_Name.setText("No Such Guest Is Available");
                Guset_Mobile.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Load_Isuue_Operation(ActionEvent event) {
        String Room_number=Room_ID_Input.getText();
        String Guest_Id= Guest_ID_Input.getText();
        
        int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to issue Room "+ Room_number+
                " to "+Guest_Name.getText(),"Confirm Issue Operation", JOptionPane.INFORMATION_MESSAGE);
        if(choice==JOptionPane.YES_OPTION){
              String str= "INSERT INTO ISSUE(Room_Number,GuestID) VALUES(" +
                "'"+ Room_number +"'," +
                "'"+ Guest_Id + "')";
        System.out.println(str);
        String str2= "UPDATE ROOMS SET isAvail = false WHERE ID = '" + Room_number + "'";
            System.out.println(str2);
            if(handler.execAction(str)&& handler.execAction(str2)){
               JOptionPane.showMessageDialog(null, "Congratulations ...Room Issue Successfully Completed!" , "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Issue Operation failed:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
            }
        } else { // if no option is selected
           JOptionPane.showMessageDialog(null, "Issue Operation Cancelled!" , "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
        }
    }

      String GuestNatID;
    @FXML
    private void LoadAllRoomInfo(ActionEvent event) {
        ObservableList <String> issuedata= FXCollections.observableArrayList();
         isReadyForSubmission = false;
        
        String G_ID= Room_Info_ID.getText();
        String quer= "SELECT * FROM ISSUE WHERE Room_Number = '"+ G_ID +"'";
        ResultSet rs= handler.execQuery(quer);
        
        try {
            while(rs.next()){
               String R_ID=  G_ID;
               GuestNatID= rs.getString("GuestID");
               Timestamp G_issue_time = rs.getTimestamp("IssueTime");
               int G_renewtimes = rs.getInt("renew_count");
               
               issuedata.add("Issue Date and Time: "+ G_issue_time.toString());
               issuedata.add("Renew Count: "+ G_renewtimes);
               
               issuedata.add("\n");
               issuedata.add("Room Information :-");
                String que= "SELECT * FROM ROOMS WHERE ID = '"+ R_ID +"'";
                ResultSet rsl= handler.execQuery(que);
                while(rsl.next()){
                    issuedata.add("Room Number : " + rsl.getString("ID"));
                    issuedata.add("Room Floor : " + rsl.getString("Floor"));
                    issuedata.add("Room_Service_One Contact : " + rsl.getString("Room_Service_One"));
                    issuedata.add("Room_Service_Two Contact : " + rsl.getString("Room_Service_Two"));          
                }
                
                issuedata.add("\n");
                issuedata.add("Guest Information :-");
                String qu= "SELECT * FROM GUESTS WHERE NationalID = '"+ GuestNatID +"'";
                ResultSet rslt= handler.execQuery(qu);
                while(rslt.next()){
                    issuedata.add("Guest Name : " + rslt.getString("Name"));
                    issuedata.add("Guest NationalID : " + rslt.getString("NationalID"));
                    issuedata.add("Guest Telephone_Number: " + rslt.getString("Telephone_Number"));
                    issuedata.add("Guest Email : " + rslt.getString("Email"));
                    issuedata.add("Guest From_Date : " + rslt.getString("From_Date"));
                    issuedata.add("Guest To_Date : " + rslt.getString("To_Date"));
                }
                isReadyForSubmission =true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Issue_Data_List.getItems().setAll(issuedata);
    }

    @FXML
    private void Load_Submission_Operation(ActionEvent event) {
        if( !isReadyForSubmission){
           JOptionPane.showMessageDialog(null, "Please Select A Room To Be Checked Out:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to check the guest out the Room? "
               ,"Confirm Issue Operation", JOptionPane.INFORMATION_MESSAGE);
        if(choice==JOptionPane.YES_OPTION){
        String Room_ID= Room_Info_ID.getText();
        String query1= "DELETE FROM ISSUE WHERE Room_Number = '"+ Room_ID +"'";
        String query2= "UPDATE ROOMS SET isAvail= true WHERE ID = '"+ Room_ID +"'";
        
        if(handler.execAction(query1)&& handler.execAction(query2)){
           JOptionPane.showMessageDialog(null, "The Guest has been succesfully checked out of the hotel room!" , "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "Checking Out Operation failed:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Operation Cancelled!" , "CANCELLED", JOptionPane.INFORMATION_MESSAGE);            
        }
    }

    @FXML
    private void LoadRenewOperation(ActionEvent event) {
      if( !isReadyForSubmission){
           JOptionPane.showMessageDialog(null, "Please Select A Room To Be Renewed:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
            return;
        }
      int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to renew room reservation for the guest? "
               ,"Confirm Issue Operation", JOptionPane.INFORMATION_MESSAGE);
       if(choice==JOptionPane.YES_OPTION){
           
        String action= "UPDATE ISSUE SET IssueTime= CURRENT_TIMESTAMP, renew_count= renew_count + 1 WHERE Room_Number = '"+ Room_Info_ID.getText() +"'";
     
        String todate= JOptionPane.showInputDialog("Please Enter The New End Of Reservation Date");
        String action2= "UPDATE GUESTS SET To_Date ='"+todate+"' WHERE NationalID = '"+ GuestNatID +"'";
        
          if(handler.execAction(action)&&handler.execAction(action2)){
            JOptionPane.showMessageDialog(null, "The Guest Room Has Been Renewed succesfully!" , "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);   
           }else{
            JOptionPane.showMessageDialog(null, "Room Renewal Operation failed:" , "Error Occured", JOptionPane.ERROR_MESSAGE);
          }
       }else{
            JOptionPane.showMessageDialog(null, "Operation Cancelled!" , "CANCELLED", JOptionPane.INFORMATION_MESSAGE);            
        }
        
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        ((Stage)rootpane.getScene().getWindow()).close();
    }

    @FXML
    private void AddMenuAddRoom(ActionEvent event) {
        loadWindow("/booking/assistant/ui/AddRoom/AddRoom.fxml","Add New Room");
    }

    @FXML
    private void AddMenuAddGuest(ActionEvent event) {
        loadWindow("/booking/assistant/ui/AddGuest/AddGuest.fxml", "Add New Hotel Guest");
    }

    @FXML
    private void LoadViewRooms(ActionEvent event) {
         loadWindow("/booking/assistant/ui/ListRooms/ListRooms.fxml","All Hotel Rooms");
    }

    @FXML
    private void LoadViewGuest(ActionEvent event) {
         loadWindow("/booking/assistant/ui/ListsGuests/ListsGuests.fxml", "All Hotel Guests");
    }

   
}
