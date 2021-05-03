/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.ListRooms;

import booking.assistant.database.DatabaseHandler;
import booking.assistant.ui.AddRoom.AddRoomController;
import booking.assistant.ui.MainWindow.MainWindowController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
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
public class ListRoomsController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableView<Room> tableView;
    @FXML
    private TableColumn<Room, String> floorCol;
    @FXML
    private TableColumn<Room, String> idCol;
    @FXML
    private TableColumn<Room, String> roomservice1Col;
    @FXML
    private TableColumn<Room, String> roomservice2Col;
    @FXML
    private TableColumn<Room, String> codeCol;
    @FXML
    private TableColumn<Room, Boolean> availabilityCol;

     ObservableList <Room> list= FXCollections.observableArrayList();
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         initCol();
        
        loadData();
    }    
//...............................................................................................................
     private void initCol() {
        floorCol.setCellValueFactory(new PropertyValueFactory<>("Floor"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        roomservice1Col.setCellValueFactory(new PropertyValueFactory<>("Room_Service1"));
        roomservice2Col.setCellValueFactory(new PropertyValueFactory<>("Room_Service2"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("Code"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("Availability"));
    }
//............................................................................................................
    private void loadData() {
     DatabaseHandler handler= DatabaseHandler.getInstance();
      String qu = "SELECT * FROM ROOMS";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String ID = rs.getString("ID");
                String Floor = rs.getString("Floor");
                String Room_Service1 = rs.getString("Room_Service_One");
                String Room_Service2 = rs.getString("Room_Service_Two");
                String Code = rs.getString("intcode");
                Boolean Availability=rs.getBoolean("isAvail");
                
                list.add(new Room(Floor,ID,Room_Service1,Room_Service2,Code,Availability));
                
               // System.out.println(Floor);
            }
        } catch (SQLException ex) { 
            Logger.getLogger(ListRoomsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);
        //tableView.getItems().setAll(list);
    }

    @FXML
    private void handleRoomDeletionOption(ActionEvent event) {
        Room room_to_be_deleted = tableView.getSelectionModel().getSelectedItem();
        if(room_to_be_deleted == null){
            JOptionPane.showMessageDialog(null, "Please select a room that you want deleted first", "No Room Deleted", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        
       int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete room  " +room_to_be_deleted.getID() + " of the floor "+ room_to_be_deleted.getFloor()
               ,"Confirm Issue Operation", JOptionPane.INFORMATION_MESSAGE);
       if(choice==JOptionPane.YES_OPTION){
           Boolean confm= DatabaseHandler.getInstance().isRoomAlreadyIssued(room_to_be_deleted);
           if(confm){
                   JOptionPane.showMessageDialog(null, " The room is already issued and cannot be deleted","Cannot Delete Already assigned Room",JOptionPane.ERROR_MESSAGE);
           }
           Boolean result = DatabaseHandler.getInstance().deleteRoom(room_to_be_deleted);
           if(result){
               JOptionPane.showMessageDialog(null, "Room "+room_to_be_deleted.getID()+ "Successfully deleted from Hotel databases");
           list.remove(room_to_be_deleted);
           }
           else{
               JOptionPane.showMessageDialog(null, "Room "+room_to_be_deleted.getID()+" Deletion was unsuccessfull!!!", "Error Occurred",JOptionPane.ERROR_MESSAGE);
           }
           
       } else{
          JOptionPane.showMessageDialog(null, "Delete Operation Cancelled!","Cancel Delete Operation",JOptionPane.PLAIN_MESSAGE);
       }
    }
//...........................................................................................................
    @FXML
    private void handleRoomEditOption(ActionEvent event) {
         Room room_to_be_edited = tableView.getSelectionModel().getSelectedItem();
        if(room_to_be_edited == null){
            JOptionPane.showMessageDialog(null, "Please select a room that you want deleted first", "No Room Deleted", JOptionPane.QUESTION_MESSAGE);
            return;
        }
        
          try {
              //getting the scene
              FXMLLoader loader= new FXMLLoader(getClass().getResource("/booking/assistant/ui/AddRoom/AddRoom.fxml"));
               Parent parent = loader.load();
               //inflating the loaded scene
               AddRoomController add_room_controller= (AddRoomController)loader.getController();
               add_room_controller.inflateUI(room_to_be_edited);
               
               Stage stage = new Stage(StageStyle.DECORATED);
               stage.setTitle("Edit Rooms Information");
               stage.setScene(new Scene(parent));
               stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//.........................................................................................................
    public static class Room{
        private final SimpleStringProperty Floor;
        private final SimpleStringProperty ID;
        private final SimpleStringProperty Room_Service1;
        private final SimpleStringProperty Room_Service2;
        private final SimpleStringProperty Code;
        private final SimpleBooleanProperty Availability;
        
    public Room(String Floor,String ID,String Room_Service1,String Room_Service2,String Code, Boolean Availability){
            this.Floor= new SimpleStringProperty(Floor);
            this.ID= new SimpleStringProperty(ID);
            this.Room_Service1= new SimpleStringProperty(Room_Service1);
            this.Room_Service2= new SimpleStringProperty(Room_Service2);
            this.Code= new SimpleStringProperty(Code);
            this.Availability= new SimpleBooleanProperty(Availability);
            
            }

        

        public String getFloor() {
            return Floor.get();
        }

        public String getID() {
            return ID.get();
        }

        public String getRoom_Service1() {
            return Room_Service1.get();
        }

        public String getRoom_Service2() {
            return Room_Service2.get();
        }

        public String getCode() {
            return Code.get();
        }

        public Boolean getAvailability() {
            return Availability.get();
        }
    }    
    
}
