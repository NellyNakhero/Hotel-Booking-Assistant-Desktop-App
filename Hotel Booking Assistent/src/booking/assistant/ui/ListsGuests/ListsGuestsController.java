/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.ListsGuests;

import booking.assistant.database.DatabaseHandler;
import booking.assistant.ui.AddGuest.AddGuestController;
import booking.assistant.ui.AddRoom.AddRoomController;
import booking.assistant.ui.ListRooms.ListRoomsController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Nelly
 */
public class ListsGuestsController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableView<Guest> tableView;
    @FXML
    private TableColumn<Guest, String> nameCol;
    @FXML
    private TableColumn<Guest, String> idCol;
    @FXML
    private TableColumn<Guest, String> telCol;
    @FXML
    private TableColumn<Guest, String> emailCol;
    @FXML
    private TableColumn<Guest, String> fromdateCol;
    @FXML
    private TableColumn<Guest, String> toDateCol;

     ObservableList <Guest> list = FXCollections.observableArrayList();
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }   
    //...............................................................................................................
    
    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Guest_Name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("National_ID"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        fromdateCol.setCellValueFactory(new PropertyValueFactory<>("Fdate"));
        toDateCol.setCellValueFactory(new PropertyValueFactory<>("Tdate"));
    }
//.............................................................................................................
      private void loadData() {
     DatabaseHandler handler= DatabaseHandler.getInstance();
      String quer = "SELECT * FROM GUESTS";
        ResultSet rs = handler.execQuery(quer);
        try {
            while (rs.next()) {
                String Guest_Name = rs.getString("Name");
                String National_ID = rs.getString("NationalID");
                String Telephone = rs.getString("Telephone_Number");
                String Email = rs.getString("Email");
                String Fdate = rs.getString("From_Date");
                String Tdate = rs.getString("To_Date");
                
             list.add(new ListsGuestsController.Guest(Guest_Name, National_ID, Telephone, Email, Fdate, Tdate) );
            }
        } catch (SQLException ex) { 
            Logger.getLogger(ListsGuestsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
       // tableView.getItems().setAll(list);
    }
//.........................................................................................................

    @FXML
    private void handleHotelGuestDeleteOperation(ActionEvent event) {
        Guest guest_to_be_deleted = tableView.getSelectionModel().getSelectedItem();
        if(guest_to_be_deleted == null){
            JOptionPane.showMessageDialog(null, "Please select a guest that you want deleted first", "No Room Deleted", JOptionPane.QUESTION_MESSAGE);
            return;
        }
       int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete hotel guest  " + guest_to_be_deleted.getGuest_Name() + " of ID number "+ guest_to_be_deleted.getNational_ID()
               ,"Confirm Issue Operation", JOptionPane.INFORMATION_MESSAGE);
       if(choice==JOptionPane.YES_OPTION){
           Boolean result= DatabaseHandler.getInstance().isGuestAlreadyIssued(guest_to_be_deleted);
       //  Boolean result = DatabaseHandler.getInstance().deleteGuestFromTable(guest_to_be_deleted);
           if(result){
               JOptionPane.showMessageDialog(null, "Hotel Guest "+ guest_to_be_deleted.getGuest_Name() + " ID number: "+ guest_to_be_deleted.getNational_ID()+ " Successfully deleted from Hotel databases");
           list.remove(guest_to_be_deleted);
           }
           else{
               JOptionPane.showMessageDialog(null, "Hotel Guest "+ guest_to_be_deleted.getGuest_Name() + "  ID number: "+ guest_to_be_deleted.getNational_ID()+" Deletion from hotel databases was unsuccessfull!!!", "Error Occurred",JOptionPane.ERROR_MESSAGE);
           }
           
       } else{
          JOptionPane.showMessageDialog(null, "Delete Operation Cancelled!","Cancel Delete Operation",JOptionPane.PLAIN_MESSAGE);
       }
    }
    
//............................................................................................................
    @FXML
    private void handleHotelGuestEditOperation(ActionEvent event) {
        ///to fetch the row gor the guest list
        Guest selected_for_edit = tableView.getSelectionModel().getSelectedItem();
    //  ListsGuestsController.Guest guest_to_be_edited = tableView.getSelectionModel().getSelectedItem();
        if(selected_for_edit == null){
            JOptionPane.showMessageDialog(null, "Please select the guest that you want deleted first", "No Guest Seleted", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        
      try {
         //getting the scene
         FXMLLoader loader= new FXMLLoader(getClass().getResource("/booking/assistant/ui/AddGuest/AddGuest.fxml"));
         Parent parent = loader.load();
         //inflating the loaded scene
         AddGuestController add_guest_controller= (AddGuestController)loader.getController();
         add_guest_controller.inflateUI(selected_for_edit);
               
         Stage stage = new Stage(StageStyle.DECORATED);
         stage.setTitle("Edit Guest Information Details");
         stage.setScene(new Scene(parent));
          stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ListsGuestsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class Guest {

        private final SimpleStringProperty Guest_Name;
        private final SimpleStringProperty National_ID;
        private final SimpleStringProperty Telephone;
        private final SimpleStringProperty Email;
        private final SimpleStringProperty Fdate;
        private final SimpleStringProperty Tdate;

        public Guest(String Guest_Name, String National_ID, String Telephone, String Email, String Fdate, String Tdate) {
            this.Guest_Name = new SimpleStringProperty(Guest_Name);
            this.National_ID = new SimpleStringProperty(National_ID);
            this.Telephone = new SimpleStringProperty(Telephone);
            this.Email = new SimpleStringProperty(Email);
            this.Fdate = new SimpleStringProperty(Fdate);
            this.Tdate = new SimpleStringProperty(Tdate);

        }

        public String getGuest_Name() {
            return Guest_Name.get();
        }

        public String getNational_ID() {
            return National_ID.get();
        }

        public String getTelephone() {
            return Telephone.get();
        }

        public String getEmail() {
            return Email.get();
        }

        public String getFdate() {
            return Fdate.get();
        }

        public String getTdate() {
            return Tdate.get();
        }

    }
    
}
