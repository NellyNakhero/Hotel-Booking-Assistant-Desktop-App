/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking.assistant.ui.AddRoom;

import booking.assistant.database.DatabaseHandler;
import booking.assistant.ui.ListRooms.ListRoomsController;
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
public class AddRoomController implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField ID;
    @FXML
    private TextField one;
    @FXML
    private TextField two;
    @FXML
    private TextField roomCode;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    
    DatabaseHandler handler;
    
    private boolean isInEditMode= Boolean.FALSE;
    
    @FXML
    private AnchorPane rootpane;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler= DatabaseHandler.getInstance();
  //      checkData();
    }    

    @FXML
    private void addRoom(ActionEvent event) {
      String RoomTitle = title.getText();
      String RoomID= ID.getText();
      String ServiceOne = one.getText();
      String ServiceTwo = two.getText();
      String RoomCode = roomCode.getText();
      
      if(RoomTitle.isEmpty()|| RoomID.isEmpty() || ServiceOne.isEmpty() || ServiceTwo.isEmpty()){
        JOptionPane.showMessageDialog(null, "Error: please enter all fields", "Error Occured", JOptionPane.ERROR_MESSAGE);
      return;
      }
      
      if (isInEditMode){
          handleEditOperation();
          return;
      }
      
       String ins= "INSERT INTO ROOMS VALUES(" +
                "'"+ RoomID +"'," +
                "'"+ RoomTitle +"'," +
                "'"+ ServiceOne +"'," +
                "'"+ ServiceTwo +"'," +
                "'"+ RoomCode +"'," +
                ""+ "true" + "" +
                ")";
        System.out.println(ins);
       if( handler.execAction(ins)){
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
        String qu = "SELECT ID FROM ROOMS";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("ID");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddRoomController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//.............................................................................................................
      public void inflateUI(ListRoomsController.Room room){
          title.setText(room.getFloor());
          ID.setText(room.getID());
          one.setText(room.getRoom_Service1());
          two.setText(room.getRoom_Service2());
          roomCode.setText(room.getCode());
          ID.setEditable(false);
          isInEditMode= Boolean.TRUE;
      }

    private void handleEditOperation() {
      ListRoomsController.Room room = new ListRoomsController.Room(title.getText(), ID.getText(), one.getText(), two.getText(), roomCode.getText(), true);
     if (handler.updatingRoom(room)){
         JOptionPane.showMessageDialog(null, "Room is Updated Successfully","Success", JOptionPane.INFORMATION_MESSAGE);
     }
     else{
         JOptionPane.showMessageDialog(null, "Operation Update Room unsuccessful","Operation Failed",JOptionPane.ERROR_MESSAGE);
     }
    }
}
